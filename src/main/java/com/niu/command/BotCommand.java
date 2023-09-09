package com.niu.command;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;


/**
 * Command接口
 * @authoer:hff
 * @Date 2023/8/2 10:42
 */
public interface BotCommand {

    //触发指令的字符
    public String command();


    //触发指令的回调
    //TODO 添加可变参数用于处理带参指令
    public Message execute(Member sender, MessageChain messageChain, Contact contact);

}
