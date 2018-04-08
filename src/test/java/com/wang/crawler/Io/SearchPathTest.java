package com.wang.crawler.Io;


import org.junit.Test;

import java.util.concurrent.ConcurrentHashMap;



public class SearchPathTest {
    SearchPath searchPath = new SearchPath();
    @Test
    public void startSearch() {
        ConcurrentHashMap result = searchPath.startSearch("/home/extend");
        System.out.println((Long)result.get("/home/extend"));
    }

    @Test
    public void processPath() {
    }
}