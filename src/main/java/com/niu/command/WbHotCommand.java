package com.niu.command;

import com.niu.anno.Command;
import com.niu.util.ApiUtil;
import com.niu.util.SeleniumUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * 微博热搜
 *
 * @authoer:hff
 * @Date 2023/9/6 11:39
 */
@Command
@Component
public class WbHotCommand implements BotCommand {

    private HashMap<Integer,String> hotMap;

    @Override
    public String command() {
        return ".hot";
    }

    @Override
    public Message execute(Member sender, MessageChain messageChain, Contact contact) {
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        if (messageChain.contentToString().contains(" ")){
            String index = messageChain.contentToString().split(" ")[1];
            try {
                SeleniumUtil.screenshot(hotMap.get(Integer.valueOf(index)),contact);
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
            return null;
        }else {
            hotMap = ApiUtil.getWbHot();
            ExternalResource externalResource = ExternalResource.create(new File("./draw-text.png"));
            Image image = contact.uploadImage(externalResource);
            try {
                externalResource.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return messageChainBuilder.append(image).build();
        }
    }
}
