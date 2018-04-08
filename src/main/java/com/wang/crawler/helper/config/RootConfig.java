package com.wang.crawler.helper.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RootConfig {

    @Bean
    public Logger logger(){
        return LoggerFactory.getLogger("com.wang.crawler");
    };
}
