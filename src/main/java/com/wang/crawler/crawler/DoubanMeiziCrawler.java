package com.wang.crawler.crawler;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;
import org.apache.http.Header;
import org.hibernate.jpa.event.internal.core.JpaSaveOrUpdateEventListener;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;

public class DoubanMeiziCrawler extends WebCrawler {
    private static final Pattern IMAGE_EXTENSIONS = Pattern.compile(".*\\.(bmp|gif|jpg|png)$");
    private static final Pattern shouldVisitUrl= Pattern.compile("https?://www.dbmeinv.com/dbgroup/\\d+");
    private final BlockingDeque<String> imgSrcQueue;

    public DoubanMeiziCrawler(BlockingDeque<String> imgSrcQueue) {
        this.imgSrcQueue = imgSrcQueue;
    }

    /**
     * You should implement this function to specify whether the given url
     * should be crawled or not (based on your crawling logic).
     */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase();
        // Ignore the url if it has an extension that matches our defined set of image extensions.
        if (IMAGE_EXTENSIONS.matcher(href).matches()) {
            return false;
        }

        if(href.contains("pager_offset")){
            return false;
        }

        // Only accept the url if it is in the "www.ics.uci.edu" domain and protocol is "http".
        return shouldVisitUrl.matcher(href).matches();
    }

    /**
     * This function is called when a page is fetched and ready to be processed
     * by your program.
     */
    @Override
    public void visit(Page page) {
        int docid = page.getWebURL().getDocid();
        String url = page.getWebURL().getURL();
        String domain = page.getWebURL().getDomain();
        String path = page.getWebURL().getPath();
        String subDomain = page.getWebURL().getSubDomain();
        String parentUrl = page.getWebURL().getParentUrl();
        String anchor = page.getWebURL().getAnchor();



        logger.debug("Docid: {}", docid);
        logger.info("URL: {}", url);
        logger.debug("Domain: '{}'", domain);
        logger.debug("Sub-domain: '{}'", subDomain);
        logger.debug("Path: '{}'", path);
        logger.debug("Parent page: {}", parentUrl);
        logger.debug("Anchor text: {}", anchor);

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            String text = htmlParseData.getText();
            String html = htmlParseData.getHtml();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();

            processGetPage(html);

            logger.debug("Text length: {}", text.length());
            logger.debug("Html length: {}", html.length());
            logger.debug("Number of outgoing links: {}", links.size());
        }

        Header[] responseHeaders = page.getFetchResponseHeaders();
        if (responseHeaders != null) {
            logger.debug("Response headers:");
            for (Header header : responseHeaders) {
                logger.debug("\t{}: {}", header.getName(), header.getValue());
            }
        }

        logger.debug("=============");
    }

    private void processGetPage(String html){
        putImgSrc(html);
    }
    private void putImgSrc(String html) {
        Logger myLogger = LoggerFactory.getLogger("com.wang.crawler");
        Document doc = Jsoup.parse(html);
        Elements imgs = doc.getElementsByTag("img");
        try{
            for(Element img:imgs){
                String src = img.attr("src");

                if(imgSrcFilter(src)) {
                    myLogger.debug(src);
                    imgSrcQueue.putLast(src);
                }

                //myLogger.debug(imgSrcQueue.element());
            }
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }

    }
    private boolean imgSrcFilter(String src){
        if(src.contains("sina") && src.contains("large")){
            return true;
        }

        return false;
    }

}


