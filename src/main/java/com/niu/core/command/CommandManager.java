package com.niu.core.command;

import com.niu.config.BotConfig;
import com.niu.core.anno.Command;
import com.niu.util.SpringUtil;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Message;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class CommandManager {

    /**
     * 通用指令集合
     */
    private static final Map<String, AnyCommand> anyCommands = new HashMap<>();
    /**
     * 群指令集合
     */
    private static final Map<String, GroupCommand> groupCommands = new HashMap<>();
    /**
     * 用户指令集合
     */
    private static final Map<String, UserCommand> userCommands = new HashMap<>();

    static {
        Map<String, Object> commandMap = SpringUtil.getBeansWithAnnotation(Command.class);
        if (commandMap != null) {
            commandMap.values().forEach(command -> {
                Command annotation = command.getClass().getAnnotation(Command.class);
                if (annotation != null) {
                    String[] names = annotation.name();
                    for (String name : names) {
                        if (command instanceof AnyCommand) {
                            anyCommands.put(name, (AnyCommand) command);
                        } else if (command instanceof UserCommand) {
                            userCommands.put(name, (UserCommand) command);
                        } else if (command instanceof GroupCommand) {
                            groupCommands.put(name, (GroupCommand) command);
                        }
                    }
                }
            });
        }
    }


    private static final BotConfig botConfig = SpringUtil.getBean(BotConfig.class);
    private static final HashSet<Character> commandHeads = botConfig.getCommandHeads();
    private static final String atBot = "@" + botConfig.getQq();
    private static final String split = botConfig.getSplit();
    private static final HashSet<Long> listeningGroup = botConfig.getListeningGroup();

    public static Message execute(GroupMessageEvent event) {
        String content = event.getMessage().contentToString();
        String[] splits;
        // 无配置指令头则不需要验证
        if (commandHeads == null) {
            splits = content.split(split);
        }
        // 配置指令头则需要验证首字符
        else if (commandHeads.contains(content.charAt(0))) {
            splits = content.substring(1).split(split);
        }
        // @机器人也不需要验证
        else if (content.startsWith(atBot)) {
            splits = content.replace(atBot, "").split(split);
        } else {
            return null;
        }

        String commandName = splits[0];
        String[] args = Arrays.copyOfRange(splits, 1, splits.length);
        AnyCommand anyCommand = anyCommands.get(commandName);
        if (anyCommand != null) {
            return anyCommand.execute(event.getSender(), event.getMessage(), event.getGroup(), args);
        }
        GroupCommand groupCommand = groupCommands.get(commandName);
        if (groupCommand != null && listeningGroup.contains(event.getGroup().getId())) {
            return groupCommand.execute(event.getSender(), event.getMessage(), event.getGroup(), args);
        }

        return null;
    }


}
