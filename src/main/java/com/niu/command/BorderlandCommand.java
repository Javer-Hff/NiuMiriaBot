package com.niu.command;

import com.niu.anno.Command;
import jakarta.annotation.PostConstruct;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.contact.Member;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * 无主之地3 红字查询
 * @authoer:hff
 * @Date 2023/8/2 14:23
 */
@Command
@Component
public class BorderlandCommand implements BotCommand {


    private static HashMap<String,String> borderlandsData = new HashMap<>();

    @PostConstruct
    public void initData() throws IOException {
        String url = "https://wiki.biligame.com/borderlands3/%E8%A3%85%E5%A4%87%E7%BA%A2%E5%AD%97%E6%8E%89%E8%90%BD%E5%AE%8C%E6%95%B4%E8%A1%A8";
        Document document = Jsoup.connect(url).get();
        String table = "//th[contains(text(),'厂商')]/../../..";
        Elements redTextElements = document.selectXpath(table + "/tbody/tr/td[not(@rowspan)][2]");
        for (Element redTextElement : redTextElements) {
            String text = redTextElement.text();
            int i = text.indexOf(" ");
            if (i!=-1){
                String substring = text.substring(0, i);
                borderlandsData.put(substring,redTextElement.nextElementSibling().text());
            }else {
                borderlandsData.put(text,redTextElement.nextElementSibling().text());
            }
        }
    }

    @Override
    public String command() {
        return ".red";
    }

    @Override
    public Message execute(Member sender, MessageChain messageChain, Contact contact) {
        MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
        String findStr = messageChain.contentToString().split("red ")[1];
        List<String> findResult = find(findStr);
        if (findResult!=null){
            messageChainBuilder.append("查询结果匹配以下红字:\n");
            for (String s : findResult) {
                messageChainBuilder.append(s).append("\n");
            }
            return messageChainBuilder.build();
        }else {
            return messageChainBuilder.append(borderlandsData.get(findStr)).build();
        }
    }

    public List<String> find(String findStr){
        Set<String> keySet = borderlandsData.keySet();
        List<String> findList = new ArrayList<>();
        if (keySet.contains(findStr)){
            return null;
        }else {
            for (String key : keySet) {
                boolean matches = Pattern.matches(".*" + findStr + ".*", key);
                if (matches){
                    findList.add(key);
                }
            }
        }
        return findList;
    }


}
