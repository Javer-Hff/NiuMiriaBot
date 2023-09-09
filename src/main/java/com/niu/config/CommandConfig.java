package com.niu.config;

import com.niu.command.BotCommand;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * TODO
 *
 * @authoer:hff
 * @Date 2023/8/2 15:00
 */
@Component
public class CommandConfig {

    public static HashMap<String, BotCommand> botCommands = new HashMap<>();


    public void registerCommand(BotCommand command){
        String commandStr = command.command();
        botCommands.put(commandStr,command);
    }

    public BotCommand getCommand(String arg){
        return botCommands.get(arg);
    }
}
