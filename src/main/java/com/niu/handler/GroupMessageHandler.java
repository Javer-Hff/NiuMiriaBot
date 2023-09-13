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
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
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
            //获取消息内容以配置中的分隔符做拆分，第一个为指令头，用于匹配，后续分割出的为指令参数
            String message = groupEvent.getMessage().contentToString();
            String[] split = message.split(botConfig.getSplit());
            BotCommand command = commandConfig.getCommand(split[0]);
            if (command!=null){
                List<String> args = Arrays.stream(split).skip(1).toList();
                Message executeResult = command.execute(groupEvent.getSender(), groupEvent.getMessage(), groupEvent.getGroup(),args.toArray(new String[0]));
                if (executeResult!=null){
                    groupEvent.getGroup().sendMessage(executeResult);
                }
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
                    InputStream inputStream = SeleniumUtil.screenshot(message);
                    Image image = ExternalResource.uploadAsImage(inputStream, groupEvent.getGroup());
                    groupEvent.getGroup().sendMessage(new MessageChainBuilder().append(image).build());
                } catch (Exception e) {
                    groupEvent.getGroup().sendMessage("请检查地址链接是否正确");
                    e.printStackTrace();
                }
            }
        }
        return ListeningStatus.LISTENING;
    }


}
