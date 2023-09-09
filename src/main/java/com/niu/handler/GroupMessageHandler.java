package com.niu.handler;

import com.niu.command.BotCommand;
import com.niu.config.BotConfig;
import com.niu.config.CommandConfig;
import com.niu.util.ApiUtil;
import com.niu.util.SeleniumUtil;
import kotlin.coroutines.CoroutineContext;
import net.mamoe.mirai.event.EventHandler;
import net.mamoe.mirai.event.ListeningStatus;
import net.mamoe.mirai.event.SimpleListenerHost;
import net.mamoe.mirai.event.events.GroupMessageEvent;
import net.mamoe.mirai.message.data.Message;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * 群消息处理器
 * @authoer:hff
 * @Date 2023/8/1 10:03
 */
@Component
public class GroupMessageHandler extends SimpleListenerHost {

    @Autowired
    private BotConfig botConfig;

    @Autowired
    private CommandConfig commandConfig;

    private static final String MATCH_PREFIX1 = "https://v2ex.com/t/";
    private static final String MATCH_PREFIX2 = "https://www.v2ex.com/t/";



    @Override
    public void handleException(@NotNull CoroutineContext context, @NotNull Throwable exception) {
        //TODO 异常处理一下
        System.out.println("=========ERROR===========");
        exception.printStackTrace();
    }

    @NotNull
    @EventHandler
    public ListeningStatus onMessage(@NotNull GroupMessageEvent groupEvent) {
        boolean isListening = botConfig.getListeningGroup().contains(groupEvent.getGroup().getId());
        //是否在监听列表中
        if (isListening){
            //获取消息内容以空格做拆分，空格前为指令，空格后为用户输入参数
            //TODO 消息和指令间的拆分符写入配置文件，从配置中读取
            String message = groupEvent.getMessage().contentToString();
            String[] split = message.split(" ");
            BotCommand command = commandConfig.getCommand(split[0]);
            if (command!=null){
                Message executeResult = command.execute(groupEvent.getSender(), groupEvent.getMessage(), groupEvent.getGroup());
                groupEvent.getGroup().sendMessage(executeResult);
            }



            //TODO 以下无指令头触发事件注册到BotEvent接口下实现
            //正则匹配bibiURL
            boolean matchesBili = Pattern.matches("^(?i)[ba]v\\d+[a-zA-Z\\d]+$", message);
            if (matchesBili){
                Message videoInfo = ApiUtil.getBiliVideoInfo(message,groupEvent.getGroup());
                if (videoInfo!=null){
                    groupEvent.getGroup().sendMessage(videoInfo);
                }
            }
            //V2ex
            boolean matchesV2 = message.startsWith(MATCH_PREFIX1) || message.startsWith(MATCH_PREFIX2);
            if (matchesV2){
                groupEvent.getGroup().sendMessage(ApiUtil.getV2TopicInfo(message));
            }
            if (!matchesBili && !matchesV2 && message.startsWith("http")){
                try {
                    SeleniumUtil.screenshot(message,groupEvent.getGroup());
                } catch (Exception e) {
                    groupEvent.getGroup().sendMessage("请检查地址链接是否正确");
                    e.printStackTrace();
                }
            }
        }
        return ListeningStatus.LISTENING;
    }


}
