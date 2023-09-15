package com.niu;

import com.niu.anno.Command;
import com.niu.command.BotCommand;
import com.niu.config.BotConfig;
import com.niu.config.CommandConfig;
import com.niu.handler.GroupMessageHandler;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.BotFactory;
import net.mamoe.mirai.internal.spi.EncryptService;
import net.mamoe.mirai.utils.BotConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import xyz.cssxsh.mirai.tool.FixProtocolVersion;
import xyz.cssxsh.mirai.tool.KFCFactory;

import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * Bot启动类
 * @authoer:hff
 * @Date 2023/8/1 9:08
 */
@Component
public class BotRunner implements CommandLineRunner  {
    @Autowired
    private BotConfig botConfig;
    @Autowired
    private GroupMessageHandler groupMessageHandler;
    @Autowired
    private CommandConfig commandConfig;
    @Autowired
    private ApplicationContext applicationContext;

    //单例Bot
    private static Bot bot;

    public static Bot getBot() {
        return bot;
    }

    @Override
    public void run(String... args) throws Exception {

        //登录热修

//        获取 8.9.63 版本协议
//        FixProtocolVersion.fetch(BotConfiguration.MiraiProtocol.ANDROID_PHONE, "8.9.63");
        //从本地加载协议文件
        FixProtocolVersion.load(BotConfiguration.MiraiProtocol.valueOf(botConfig.getProtocol()));
        //Spi加载
        ServiceLoader.load(EncryptService.class);
        ServiceLoader.load(EncryptService.Factory.class);
        KFCFactory.install();

        System.out.println(">>>>>>>>>>start>>>>>>>>>>");

        List<Long> listeningGroup = botConfig.getListeningGroup();
        for (Long groupId : listeningGroup) {
            System.out.println("listening group:" + groupId);
        }

        BotConfiguration config = new BotConfiguration(){
            {   //登录指纹存储本地文件，避免每次登录校验
                fileBasedDeviceInfo(botConfig.getWorkdir() + "deviceInfo.json");
                setProtocol(MiraiProtocol.valueOf(botConfig.getProtocol()));
            }
        };

        FixProtocolVersion.sync(BotConfiguration.MiraiProtocol.valueOf(botConfig.getProtocol()));
//        //登录方式为扫码
//        bot = BotFactory.INSTANCE.newBot(botConfig.getQq(), BotAuthorization.byQRCode(),config);
        bot = BotFactory.INSTANCE.newBot(botConfig.getQq(),botConfig.getPassword(),config);
        //注册事件监听器
        bot.getEventChannel().registerListenerHost(groupMessageHandler);
        //注册指令
        Map<String, Object> commands = applicationContext.getBeansWithAnnotation(Command.class);
        commands.values().forEach(o->commandConfig.registerCommand((BotCommand) o));

        bot.login();

        System.out.println(">>>>>>>>>>running>>>>>>>>>>");
    }
}
