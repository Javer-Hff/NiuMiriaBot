package com.niu;

import com.niu.config.BotConfig;
import com.niu.handler.GroupMessageHandler;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import top.mrxiaom.qsign.QSignService;
import xyz.cssxsh.mirai.tool.FixProtocolVersion;

import java.io.File;
import java.util.HashSet;

/**
 * Bot启动类
 *
 * @authoer:hff
 * @Date 2023/8/1 9:08
 */
@Component
@RequiredArgsConstructor
public class BotRunner implements CommandLineRunner {
    private final BotConfig botConfig;
    private final GroupMessageHandler groupMessageHandler;

    //单例Bot
    @Getter
    private static Bot bot;

    @Override
    public void run(String... args) {
        // 加载修复插件
        FixProtocolVersion.update();
        // 加载签名服务
        QSignService.Factory.init(new File("txlib/8.9.70"));
        QSignService.Factory.loadProtocols(null);
        QSignService.Factory.register();

        System.out.println(">>>>>>>>>>start>>>>>>>>>>");

        HashSet<Long> listeningGroup = botConfig.getListeningGroup();
        for (Long groupId : listeningGroup) {
            System.out.println("listening group:" + groupId);
        }

        BotConfiguration config = new BotConfiguration() {
            {   //登录指纹存储本地文件，避免每次登录校验
                fileBasedDeviceInfo(botConfig.getWorkdir() + "deviceInfo.json");
                setProtocol(MiraiProtocol.valueOf(botConfig.getProtocol()));
            }
        };

        bot = BotFactory.INSTANCE.newBot(botConfig.getQq(), botConfig.getPassword(), config);
        //注册事件监听器
        bot.getEventChannel().registerListenerHost(groupMessageHandler);

        bot.login();

        System.out.println(">>>>>>>>>>running>>>>>>>>>>");
    }
}
