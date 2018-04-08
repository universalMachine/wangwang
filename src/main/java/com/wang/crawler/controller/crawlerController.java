package com.wang.crawler.controller;

import com.wang.crawler.service.DoubanmeiziService;
import com.wang.crawler.service.RedditService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class crawlerController {
    @Autowired
    private static DoubanmeiziService doubanmeiziService = new DoubanmeiziService();

    private static RedditService redditService = new RedditService();
    @Autowired
    private Logger logger;

    @GetMapping("/doubanMeizi")
    public void doubanMeiziCrawler(){
        try{
            //doubanmeiziService.startCrawler();
            doubanmeiziService.crawlerRank();
        }catch (Exception e){
            logger.debug("crawler doubanmeizi failed");
        }

    }

    public static void main(String[] args) throws Exception {
        start();
    }

    private static  void start() throws Exception{
       // doubanmeiziService.startCrawler();
        //doubanmeiziService.crawlerRank();
        redditService.crawlerHot(200,200);
    }
}
