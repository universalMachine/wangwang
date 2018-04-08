package com.wang.crawler.Io;


import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.PosixFileAttributes;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class SearchPath {
    private final ConcurrentHashMap<String,Long> result = new ConcurrentHashMap<>(104288, 0.75f, 3000);
    private final ConcurrentHashMap<String,String> filterResult = new ConcurrentHashMap<>(5288, 0.75f, 3000);

    //private final CountDownLatch latch = new CountDownLatch(0);
    private final Phaser phaser = new Phaser(1);
    private final PhaserExecutorService phaserExecutorService = new PhaserExecutorService(12,phaser);
    public static void main(String[] args) {
        SearchPath searchPath = new SearchPath();
        //  searchPath.phaser.arriveAndAwaitAdvance();
        ConcurrentHashMap result = searchPath.startSearch("/home/extend");
        System.out.println((Long)result.get("/home/extend"));
        searchPath.filterMap(500);
        System.out.println(searchPath.filterResult);
        searchPath.phaserExecutorService.shutdown();
    }


    private Callback emptyCallback = new Callback() {
        @Override
        public void callback(long result) {
            return;
        }
    };

    public ConcurrentHashMap startSearch(String path){

        processPath(path,emptyCallback);

        phaser.arriveAndAwaitAdvance();
        return  result;


    }
    public void processPath(String path,Callback prevCallback){
        //link whole file system
        if(path.equals("/home/extend/.wine/dosdevices/z:")){
            return;
        }
        phaser.register();
        result.putIfAbsent(path,new Long(0));
        Callback  currentCallback = new Callback() {
            @Override
            public void callback(long res) {
                prevCallback.callback(res);
                //result.put(path,result.get(path)+res);
                result.computeIfPresent(path,(key,value)->{
                    return value + res;
                });
            }
        };


            Path dir = Paths.get(path).normalize();


        try(DirectoryStream<Path> stream= Files.newDirectoryStream(dir)){
            for(Path entry:stream){
                if(Files.isDirectory(entry)){
                    futureCallback(entry,currentCallback);
                }else{
                    if(Files.isSymbolicLink(entry)){
                        PosixFileAttributes posixFileAttributes = Files.readAttributes(entry,PosixFileAttributes.class, LinkOption.NOFOLLOW_LINKS);
                        currentCallback.callback(posixFileAttributes.size());

                    }else {
                        currentCallback.callback(Files.size(entry));
                    }

                }
            }
        }catch (IOException e){
            e.printStackTrace();

        }finally {
            phaser.arriveAndDeregister();
        }


    }

    public void futureCallback(Path entry,Callback callback){
        phaser.register();
        phaserExecutorService.submit(new Callable() {
            @Override
            public Integer call() {
                processPath(entry.toString(),callback);
                return 0;
            }
        });
    }


    public void filterMap(long threshold){
        long thresholdByte = threshold * 1000 * 1000;
        Integer len = result.size();
        ArrayList<String> keys = Collections.list(result.keys());

        AtomicInteger count = new AtomicInteger(1);
        Integer totalSegment = 10;
        Integer perSize = len /totalSegment;
        Integer i =1;
        for(;i<=totalSegment;i++){
            Integer currentSegment = i;
            Integer  startPos = (i-1)*perSize+1;
            phaser.register();
            Callable task= new Callable<Integer>() {
                @Override
                public Integer call() {
                    try {
                        if(isLastSegment(currentSegment,totalSegment)){
                            for(int x=startPos;x<=len;x++){
                                result.compute(keys.get(x-1),(k,v)->{
                                    if(v>=thresholdByte){
                                        filterResult.putIfAbsent(k,Long.toString(v/1000/1000)+"M");
                                    }
                                    return  v;
                                });
                            }
                        }else{
                            for(int x=startPos;x<=startPos+perSize;x++){
                                result.compute(keys.get(x-1),(k,v)->{
                                    if(v>=thresholdByte){
                                        filterResult.putIfAbsent(k,Long.toString(v/1000/1000)+"M");
                                    }
                                    return  v;
                                });
                            }
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                    return  0;
                }
            };
            phaserExecutorService.submit(task);
        }


        phaser.arriveAndAwaitAdvance();

    }

    private  boolean isLastSegment(Integer cursor,Integer totalSegment){
        return cursor == totalSegment;
    }
}
