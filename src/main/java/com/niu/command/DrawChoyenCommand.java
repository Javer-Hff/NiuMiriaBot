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
 * 500choyen(https://github.com/yurafuca/5000choyen
 * @authoer:hff
 * @Date 2023/9/1 15:41
 */
@Command
@Component
public class DrawChoyenCommand implements BotCommand{
    @Override
    public String command() {
        return ".choyen";
    }

    @Override
    public Message execute(Member sender, MessageChain messageChain, Contact contact) {
        String messageStr = messageChain.contentToString();
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        try {
            int commandStart = messageStr.indexOf(".choyen");
            String arg = messageStr.substring(commandStart+8);
            String[] split = arg.trim().split(" ");
            if (SkikoUtil.drawChoyen(split[0],split[1])){
                ExternalResource externalResource = ExternalResource.create(new File("./draw-choyen.png"));
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
