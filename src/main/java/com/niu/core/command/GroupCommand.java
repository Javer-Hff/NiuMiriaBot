package com.niu.core.command;

import net.mamoe.mirai.contact.Group;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;


/**
 * 适用于群的指令接口
 */
public interface GroupCommand {

    /**
     * 执行指令
     *
     * @param sender       群成员
     * @param messageChain 消息链
     * @param group        群
     * @param args         追加参数
     * @return 指令执行后的返回消息
     */
    Message execute(Member sender, MessageChain messageChain, Group group, String... args);

}
