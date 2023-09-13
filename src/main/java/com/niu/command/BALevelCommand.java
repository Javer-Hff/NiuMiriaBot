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

@Component
@Command
public class BALevelCommand implements BotCommand {
    @Override
    public String command() {
        return ".怎么过";
    }

    @Override
    public Message execute(Member sender, MessageChain messageChain, Contact contact) {
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        String content = messageChain.contentToString();
        int commandStart = content.indexOf(command());
        String arg = content.substring(commandStart+command().length()+1);
        String href = ApiUtil.getBAIntro(arg);
        if (href.startsWith("http")){
            try {
                SeleniumUtil.screenshot(href,contact);
                return null;
            } catch (InterruptedException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return messageChainBuilder.append(href).build();
    }
}

