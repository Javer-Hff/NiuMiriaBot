package com.niu.command;

import com.niu.anno.Command;
import com.niu.util.SkikoUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import java.io.File;

/**
 * pornhub
 * @authoer:hff
 * @Date 2023/8/28 15:31
 */
@Command
@Component
public class DrawHubCommand implements BotCommand{

    @Override
    public String command() {
        return ".porn";
    }

    @Override
    public Message execute(Member sender, MessageChain messageChain, Contact contact,String...args) {
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        try {
            if (SkikoUtil.drawPornHub(args[0],args[1])){
                ExternalResource externalResource = ExternalResource.create(new File("./draw-porn.png"));
                Image image = contact.uploadImage(externalResource);
                externalResource.close();
                return messageChainBuilder.append(image).build();
            }
            return messageChainBuilder.append("啊？").build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
