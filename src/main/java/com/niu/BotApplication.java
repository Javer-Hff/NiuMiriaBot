package com.niu;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

/**
 * @authoer:hff
 * @Date 2023/8/1 8:54
 */
@SpringBootApplication
@EnableCaching
public class BotApplication {
    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(BotApplication.class,args);
    }
}
