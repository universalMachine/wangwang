package com.wang.crawler.crawler;

import com.gargoylesoftware.htmlunit.ProxyConfig;
import com.gargoylesoftware.htmlunit.ScriptPreProcessor;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.http.Header;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class RedditCrawler  {
    private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");
    //private static final Pattern shouldVisitUrl= Pattern.compile("https?://www.dbmeinv.com/dbgroup/\\d+");
    private final BlockingDeque<String> imgSrcQueue;

    public RedditCrawler(BlockingDeque<String> imgSrcQueue) {
        this.imgSrcQueue = imgSrcQueue;
    }



    private String pageFetch(String url){
        try{
            WebClient webClient = new WebClient();

            ProxyConfig proxyConfig = new ProxyConfig();
            proxyConfig.setProxyHost("127.0.0.1");
            proxyConfig.setProxyPort(1080);
            proxyConfig.setSocksProxy(true);
            webClient.getOptions().setProxyConfig(proxyConfig);
            webClient.getOptions().setTimeout(8000);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.waitForBackgroundJavaScript(3000);


            webClient.setScriptPreProcessor(new ScriptPreProcessor() {
                @Override
                public String preProcess(HtmlPage htmlPage, String s, String s1, int i, HtmlElement htmlElement) {
                  if(s1.contains("google")||s1.contains("amzon"))
                    return "";
                  else
                    return s;
                }
            });


            HtmlPage page = webClient.getPage(url);
            return page.getWebResponse().getContentAsString();
        }catch (Exception e){
            e.printStackTrace();
            return "";
        }

    }

    public void startCrawler(Set<String> urls){
        final AtomicInteger crawlerdUrlLength = new AtomicInteger(0);
        final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        List<String> list = new ArrayList<>();
        list.addAll(urls);

        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try{
                    if(crawlerdUrlLength.get() == urls.size())
                        return;
                    String html =  pageFetch(list.get(crawlerdUrlLength.incrementAndGet() -1));
                    processGetPage(html);
                }catch (Exception e){
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }

            }
        },200,200, TimeUnit.MILLISECONDS);
    }


    private void processGetPage(String html){
        putImgSrc(html);
    }
    private void putImgSrc(String html) {
        Logger myLogger = LoggerFactory.getLogger("com.wang.crawler");
        Document doc = Jsoup.parse(html);
        Elements as = doc.getElementsByTag("a");
        List<Element> imgAs = new ArrayList<>();
        try{

            for(Element a: as){
                if(  a.classNames().contains("thumbnail")&&imgSrcFilter(a.attr("href"))) {
                    imgAs.add(a);
                }
            }
            for(Element a:imgAs){

                String full = a.attr("href");

                Element img =  a.getElementsByTag("img").first();
                if(img != null){
                    String thumb = "https:"+img.attr("src");
                    imgSrcQueue.putLast(EncodeImage.encodeImg(thumb,full));
                    myLogger.debug(thumb);
                    myLogger.debug(full);
                }

            }

                //myLogger.debug(imgSrcQueue.element());
        } catch (Exception e){
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }

    }
    private boolean imgSrcFilter(String src){
        if(IMAGE_EXTENSIONS.matcher(src).matches()){
            return true;
        }

        return false;
    }

}


