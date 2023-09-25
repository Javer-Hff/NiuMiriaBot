package com.niu.core.command;

import net.mamoe.mirai.contact.User;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;

/**
 * 适用于用户的指令接口
 */
public interface UserCommand {

    /**
     * 执行指令
     *
     * @param user         用户
     * @param messageChain 消息链
     * @param args         追加参数
     * @return 指令执行后的返回消息
     */
    Message execute(User user, MessageChain messageChain, String... args);
}
