package com.niu.config;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * 读取Bot配置
 * @authoer:hff
 * @Date 2023/8/1 9:34
 */
@ConfigurationProperties(prefix = "bot")
@SpringBootConfiguration
@Data
public class BotConfig {
    private long qq;
    private String password;
    private String protocol;
    private List<Long> listeningGroup;
}
