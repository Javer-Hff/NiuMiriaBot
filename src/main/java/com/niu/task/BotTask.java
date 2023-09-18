package com.niu.task;

import com.niu.BotRunner;
import com.niu.util.ImageDownloadUtil;
import net.mamoe.mirai.Bot;
import net.mamoe.mirai.contact.ContactList;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

/**
 * 新闻推送定时任务
 * @authoer:hff
 * @Date 2023/8/2 16:27
 */
@Component
@EnableScheduling
public class BotTask {

    @Scheduled(cron = "0 0 8 * * ?")
    public void sendNews(){
        try {
            ImageDownloadUtil.downloadNews();
            Bot bot = BotRunner.getBot();
            for (Group group : bot.getGroups()) {
                ExternalResource externalResource = ExternalResource.create(new File("./60s.jpg"));
                Image image = group.uploadImage(externalResource);
                externalResource.close();
                MessageChain messages = new MessageChainBuilder().append(image).build();
                group.sendMessage(messages);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "0 30 13 * * ?")
    public void sendCalendar(){
        try {
            if (ImageDownloadUtil.downloadMoyu()){
                Bot bot = BotRunner.getBot();
                for (Group group : bot.getGroups()) {
                    ExternalResource externalResource = ExternalResource.create(new File("./moyu.jpg"));
                    Image image = group.uploadImage(externalResource);
                    externalResource.close();
                    MessageChain messages = new MessageChainBuilder().append(image).build();
                    group.sendMessage(messages);
                }
            }else {
                System.out.println("moyu down error");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Scheduled(cron = "0 0 7 * * ?")
    public void dayCalendar() throws IOException {
        Bot bot = BotRunner.getBot();
        ContactList<Group> groups = bot.getGroups();
        DayOfWeek day = LocalDateTime.now().getDayOfWeek();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("image/day/" + day.getValue() + ".jpg");
        ExternalResource resource = ExternalResource.create(stream, "jpg");
        for (Group group : groups) {
            Image image = group.uploadImage(resource);
            MessageChain messages = new MessageChainBuilder().append(image).build();
            group.sendMessage(messages);
        }
        resource.close();
    }



}
