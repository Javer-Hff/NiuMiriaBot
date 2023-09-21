package com.niu.command;


import com.niu.core.command.AnyCommand;
import com.niu.core.anno.Command;
import com.niu.util.ApiUtil;
import com.niu.util.SeleniumUtil;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.IOException;
import java.io.InputStream;

@Command(name = {"怎么过"})
public class BALevelCommand implements AnyCommand {

    @Override
    public Message execute(User sender, MessageChain messageChain, Contact contact, String... args) {
        String arg = args[0];
        String baContentUrl = ApiUtil.getBaContentUrl(arg);
        try {
            InputStream inputStream = SeleniumUtil.screenshot(baContentUrl);
            if (inputStream != null) {
                Image image = ExternalResource.uploadAsImage(inputStream, contact);
                inputStream.close();
                return new MessageChainBuilder().append(image).build();
            }
            return new MessageChainBuilder().append("啊？").build();
        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

}

