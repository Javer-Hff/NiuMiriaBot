package com.niu.config;

import lombok.Data;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashSet;

/**
 * 读取Bot配置
 *
 * @authoer:hff
 * @Date 2023/8/1 9:34
 */
@ConfigurationProperties(prefix = "bot")
@SpringBootConfiguration
@Data
public class BotConfig {
    //qq
    private long qq;
    //密码
    private String password;
    //协议
    private String protocol;
    //v站token
    private String v2token;
    //工作目录
    private String workdir;
    //命令参数分隔符
    private String split;
    //指令头
    private HashSet<Character> commandHeads;
    //代理IP
//    @Value("${bot.proxy.host}")
//    private String host;
//    //代理端口
//    @Value("${bot.proxy.port}")
//    private int port;
    //监听群组列表
    private HashSet<Long> listeningGroup;
}
