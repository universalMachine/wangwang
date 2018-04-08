package com.wang.crawler.service;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class DoubanmeiziServiceTest {
    DoubanmeiziService doubanmeiziService;

    @Before
    public void setUp() throws Exception {
        doubanmeiziService = new DoubanmeiziService();
    }

    @Test
    public void startCrawler() throws Exception{
        doubanmeiziService.startCrawler();
    }

    @Test
    public void getPaginationById() {
        assertArrayEquals( doubanmeiziService.getPaginationById(1,2,3).toArray(),
                Arrays.asList("https://www.dbmeinv.com/dbgroup/show.htm?cid=1&pager_offset=2",
                        "https://www.dbmeinv.com/dbgroup/show.htm?cid=1&pager_offset=3"
                        ).toArray());
    }
    @Test
    public void getRankPagination() {
        assertArrayEquals( doubanmeiziService.getRankPagination(2,3).toArray(),
                Arrays.asList("https://www.dbmeinv.com/dbgroup/rank.htm?pager_offset=2",
                        "https://www.dbmeinv.com/dbgroup/rank.htm?pager_offset=3"
                ).toArray());
    }

}