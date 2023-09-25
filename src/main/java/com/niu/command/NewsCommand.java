package com.niu.command;

import com.niu.core.anno.Command;
import com.niu.core.command.GroupCommand;
import com.niu.util.ImageDownloadUtil;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.File;

/**
 * 60s看世界
 * @authoer:hff
 * @Date 2023/8/2 10:33
 */
@Command(name = {"60s"})
public class NewsCommand implements GroupCommand {

    @Override
    public Message execute(Member sender, MessageChain messageChain, Group group, String... args) {
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        try {
            if (ImageDownloadUtil.downloadNews()){
                ExternalResource externalResource = ExternalResource.create(new File("./60s.jpg"));
                Image image = group.uploadImage(externalResource);
                externalResource.close();
                return messageChainBuilder.append(image).build();
            }
            return messageChainBuilder.append("啊？").build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
