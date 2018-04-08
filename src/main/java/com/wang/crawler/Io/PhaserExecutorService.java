package com.wang.crawler.Io;

import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.ExecutorServiceAdapter;

import java.util.concurrent.*;

public class PhaserExecutorService  extends ThreadPoolExecutor{
    private  final Phaser phaser;



    public PhaserExecutorService(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, BlockingQueue<Runnable> workQueue,Phaser phaser) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
        this.phaser = phaser;
    }

    public PhaserExecutorService(int nThreads,Phaser phaser) {
        this(nThreads, nThreads,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(),phaser);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new PhaserFuture(callable,phaser);
    }


}
