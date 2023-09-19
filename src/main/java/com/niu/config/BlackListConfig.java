package com.niu.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "bot.black-list")
@SpringBootConfiguration
@Data
public class BlackListConfig {

    //只要链接包含则屏蔽
    private List<String> webSite;

    @Value("return-words")
    private String returnWords;

}
