package com.wang.crawler.wordpress;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.gargoylesoftware.htmlunit.HttpMethod;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebRequest;
import com.sun.deploy.config.ClientConfig;

import javax.xml.bind.DatatypeConverter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;


public class WordPressService {


    public void getPost() throws Exception{
        WebClient webClient = new WebClient();
        setCredentials(webClient);
        URL url = new URL("https://localhost:23233/wp-json/wp/v2/posts/");
        WebRequest requestSettings = new WebRequest(url, HttpMethod.GET);
        ObjectMapper objectMapper = new ObjectMapper();



        Post post = new Post();
        post.setAuthor(1);
        post.setTitle("test");
        post.setDate(new Date());
        post.setFeatured_image("https://ww2.sinaimg.cn/bmiddle/0060lm7Tgy1fpjk64up39j30dw0hs0vm.jpg");

        //requestSettings.setRequestBody(objectMapper.writeValueAsString(post));

        Page redirectPage = webClient.getPage(requestSettings);
        System.out.println(redirectPage.getWebResponse().getContentAsString());
    }

    public void addPost(String content, Object ...values) throws Exception{
        WebClient webClient = new WebClient();
        setCredentials(webClient);

        URL url = new URL("https://www.beautyasiagirl.com/wp-json/wp/v2/posts");
        WebRequest requestSettings = new WebRequest(url, HttpMethod.POST);
        ObjectMapper objectMapper = new ObjectMapper();

        requestSettings.setAdditionalHeader("Accept", "*/*");
        requestSettings.setAdditionalHeader("Content-Type", "application/x-www-form-urlencoded;; charset=UTF-8");
        requestSettings.setAdditionalHeader("Referer", "REFURLHERE");
        //requestSettings.setAdditionalHeader("Accept-Language", "en-US,en;q=0.8");
        requestSettings.setAdditionalHeader("Accept-Encoding", "gzip,deflate,sdch");
        //requestSettings.setAdditionalHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
        requestSettings.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
        requestSettings.setAdditionalHeader("Cache-Control", "no-cache");
        requestSettings.setAdditionalHeader("Pragma", "no-cache");
        //requestSettings.setAdditionalHeader("Origin", "http://localhost:23233");

        Post post = new Post();
        //post.setAuthor(1);
        post.setTitle("who is most beauty asian girl woman photo,where are hot asian girl");
        post.setDate(new Date());
        post.setContent(content);
        if(values.length>0){
            post.addCategories(values[0].toString());
        }

       // post.setFeatured_image("https://ww2.sinaimg.cn/bmiddle/0060lm7Tgy1fpjk64up39j30dw0hs0vm.jpg");

        requestSettings.setRequestBody(post.toFormData());

        Page redirectPage = webClient.getPage(requestSettings);
        System.out.println(redirectPage.getWebResponse().getContentAsString());
    }

    private static void setCredentials(WebClient webClient)
    {
        String username = "extend";
        String password = "abao321";
        String base64encodedUsernameAndPassword = base64Encode(username + ":" + password);
        webClient.addRequestHeader("Authorization", "Basic " + base64encodedUsernameAndPassword);
    }

    private static String base64Encode(String stringToEncode)
    {
        return DatatypeConverter.printBase64Binary(stringToEncode.getBytes());
    }
}
