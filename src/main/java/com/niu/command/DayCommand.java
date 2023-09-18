package com.niu.command;

import com.niu.anno.Command;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Component
@Command
public class DayCommand implements BotCommand {
    @Override
    public String command() {
        return "今天周几";
    }

    @Override
    public Message execute(Member sender, MessageChain messageChain, Contact contact, String... args) {
        DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("image/day/" + dayOfWeek.getValue() + ".jpg");
        Image image = ExternalResource.uploadAsImage(stream,contact,"jpg");
        return new MessageChainBuilder().append(image).build();
    }
}
