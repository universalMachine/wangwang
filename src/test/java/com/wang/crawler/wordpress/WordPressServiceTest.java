package com.wang.crawler.wordpress;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class WordPressServiceTest {
    WordPressService wordPressService;
    @Before
    public void setUp() throws Exception {
        wordPressService = new WordPressService();
    }

    @Test
    public void getPost() {
        try{
            wordPressService.getPost();
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    @Test
    public void addPost() {
        try{
            wordPressService.addPost("https://ww1.sinaimg.cn/large/0060lm7Tgy1fppc9tv4srj30dw0ii0vx.jpg");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
}