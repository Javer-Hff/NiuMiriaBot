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
import java.io.InputStream;

@Component
@Command
public class BALevelCommand implements BotCommand {
    @Override
    public String command() {
        return "怎么过";
    }

    @Override
    public Message execute(Member sender, MessageChain messageChain, Contact contact,String...args) {
        String arg = args[0];
        String baContentUrl = ApiUtil.getBaContentUrl(arg);
        try {
            InputStream inputStream = SeleniumUtil.screenshot(baContentUrl);
            Image image = ExternalResource.uploadAsImage(inputStream, contact);
            return new MessageChainBuilder().append(image).build();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}

