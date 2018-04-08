package com.wang.crawler.service;

import com.gargoylesoftware.htmlunit.WebClient;
import com.wang.crawler.crawler.DoubanMeiziCrawler;
import com.wang.crawler.wordpress.WordPressService;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.crawler.authentication.AuthInfo;
import edu.uci.ics.crawler4j.crawler.authentication.FormAuthInfo;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.*;

import static java.lang.Thread.sleep;

@Service
public class DoubanmeiziService {



    private  final BlockingDeque<String> queue = new LinkedBlockingDeque<>(1000);
    private final WordPressService wordPressService = new WordPressService();

    public  void startCrawler() throws Exception{

        String crawlStorageFolder = "/tmp/crawl";
        final String androidUA = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Mobile Safari/537.36)";

        Integer numberOfCrawlers = 5;
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setPolitenessDelay(500);

       /*  * Instantiate the controller for this crawl.*/


        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setUserAgentName(androidUA);
        config.setUserAgentString(androidUA);
        config.setMaxDepthOfCrawling(1);



        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);


  /*       * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages*/

      Set<String> pagingUrls=getPaginationById(2,1,2);
      pagingUrls.forEach(url->controller.addSeed(url));


        processCrawlResult();
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */

        CrawlController.WebCrawlerFactory webCrawlerFactory=new CrawlController.WebCrawlerFactory(){

            @Override
            public WebCrawler newInstance() throws Exception {
                return new DoubanMeiziCrawler(queue);
            }
        };


        controller.start(webCrawlerFactory, numberOfCrawlers);
    }

    public  void crawlerRank() throws Exception{

        String crawlStorageFolder = "/tmp/crawl";
        final String androidUA = "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Mobile Safari/537.36)";

        Integer numberOfCrawlers = 5;
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);
        config.setPolitenessDelay(500);

        /*  * Instantiate the controller for this crawl.*/


        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        robotstxtConfig.setUserAgentName(androidUA);
        config.setUserAgentString(androidUA);
        config.setMaxDepthOfCrawling(1);
   /*     AuthInfo authInfo = new FormAuthInfo("kfjateqr@mail.bccto.me", "doubanmeizi", "https://www.dbmeinv.com/dbgroup/login.htm", String usernameFormStr, String passwordFormStr);
        config.addAuthInfo(authInfo);*/


        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);


        /*       * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages*/

        Set<String> pagingUrls=getRankPagination(1,1);
        pagingUrls.forEach(url->controller.addSeed(url));


        processCrawlResult();
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */

        CrawlController.WebCrawlerFactory webCrawlerFactory=new CrawlController.WebCrawlerFactory(){

            @Override
            public WebCrawler newInstance() throws Exception {
                return new DoubanMeiziCrawler(queue);
            }
        };


        controller.start(webCrawlerFactory, numberOfCrawlers);
    }

    private void processCrawlResult(){
        final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(5);
        executorService.scheduleWithFixedDelay(new Runnable() {
            @Override
            public void run() {
                try{
                    //while(true){
                        wordPressService.addPost(queue.takeFirst());
                    //}

                }catch (Exception e){
                    Thread.currentThread().interrupt();
                }

            }
        },1,1,TimeUnit.SECONDS);
    }

    private Set<String> getPagination(String prefix,Integer pageStart,Integer pageEnd){
        Set<String> seeds = new HashSet();
        //String url = "https://www.dbmeinv.com/dbgroup/show.htm";
        //WebClient webClient = new WebClient();
        //webClient.getPage()
        for(int i=pageStart;i<=pageEnd;i++){
            seeds.add(prefix+"pager_offset="+i);
        }
        return seeds;

    }

    public Set<String> getPaginationById(Integer id,Integer pageStart,Integer pageEnd){
        Set<String> seeds = new HashSet();
        String url = "https://www.dbmeinv.com/dbgroup/show.htm?cid="+id+"&";
        return getPagination(url,pageStart,pageEnd);

    }

    public Set<String> getRankPagination(Integer pageStart,Integer pageEnd){
        Set<String> seeds = new HashSet();
        String url = "https://www.dbmeinv.com/dbgroup/rank.htm?";
        return getPagination(url,pageStart,pageEnd);

    }

}
