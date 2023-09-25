package com.niu.core.command;

import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;

/**
 * 适用于所有消息指令接口
 */
public interface AnyCommand {

    /**
     * 执行指令
     *
     * @param user         消息发送人
     * @param messageChain 消息链
     * @param contact      联系人
     * @param args         追加参数
     * @return 指令执行后的返回消息
     */
    Message execute(User user, MessageChain messageChain, Contact contact, String... args);

}
