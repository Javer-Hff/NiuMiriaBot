package com.niu.command;

import com.niu.core.anno.Command;
import com.niu.core.command.GroupCommand;
import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;

import java.io.InputStream;
import java.time.DayOfWeek;
import java.time.LocalDateTime;

@Command(name = {"今天周几"})
public class DayCommand implements GroupCommand {

    @Override
    public Message execute(Member sender, MessageChain messageChain, Group group, String... args) {
        DayOfWeek dayOfWeek = LocalDateTime.now().getDayOfWeek();
        InputStream stream = this.getClass().getClassLoader().getResourceAsStream("image/day/" + dayOfWeek.getValue() + ".jpg");
        Image image = ExternalResource.uploadAsImage(stream, group, "jpg");
        return new MessageChainBuilder().append(image).build();
    }

}
