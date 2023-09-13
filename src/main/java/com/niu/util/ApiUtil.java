package com.niu.util;

import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.niu.bean.WbHotObject;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.Message;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * API接口工具类
 *
 * @authoer:hff
 * @Date 2023/9/6 11:40
 */
@Component
public class ApiUtil {

    private static final String WB_API = "https://api.vvhan.com/api/wbhot";

    private static final String BILI_API="https://api.bilibili.com/x/web-interface/view";

    private static final String NEWS_API = "http://dwz.2xb.cn/zaob";

    private static final String MOYU_API ="https://api.vvhan.com/api/moyu?type=json";

    private static final String V2EX_API = "https://www.v2ex.com/api/v2/topics/";

    private static final String BA_API = "https://ba.gamekee.com";

    private static final OkHttpClient HTTP_CLIENT = new OkHttpClient();

    private static Map<String,String> baCache = new HashMap<>();


    private static String v2Token;
    @Value("${bot.v2token}")
    public void setV2Token(String v2Token){
        ApiUtil.v2Token = v2Token;
    }

    public static HashMap<Integer, WbHotObject> getWbHot(){
        Request request = new Request.Builder().url(WB_API).get().build();
        try {
            Response response = HTTP_CLIENT.newCall(request).execute();
            String respJson = response.body().string();
            JSONObject jsonObject = JSONUtil.parseObj(respJson);
            JSONArray dataArray = jsonObject.getJSONArray("data");
            HashMap<Integer, WbHotObject> resultMap = new HashMap<>();
            int i = 1;
            for (Object o : dataArray) {
                JSONObject element = JSONUtil.parseObj(o.toString());
                String title = element.getStr("title");
                String url = element.getStr("url");
                String hot = element.getStr("hot");
                WbHotObject object = new WbHotObject(i,title,"热度:"+hot,url);
                resultMap.put(i,object);
                i++;
            }
            return resultMap;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Message getBiliVideoInfo(String videoId, Contact contact){
        String lowerCaseId = videoId.toLowerCase();
        String requestParam = lowerCaseId.startsWith("bv")?"bvid=" + videoId:"aid=" + videoId.replace("av","")
                .replace("AV","");
        Request request = new Request.Builder().url(BILI_API + "?" + requestParam).build();
        try {
            Response response = HTTP_CLIENT.newCall(request).execute();
            if (response.isSuccessful()&&response.body()!=null){
                String jsonString = response.body().string();
                JSONObject responseBody = JSONUtil.parseObj(jsonString);

                JSONObject data = responseBody.getJSONObject("data");
                JSONObject owner = data.getJSONObject("owner");
                JSONObject stat = data.getJSONObject("stat");

                String pic = data.getStr("pic");
                String title = data.getStr("title");
                Double duration = data.getDouble("duration");
                String desc = data.getStr("desc");
                if (desc.length()>100){
                    desc = desc.substring(0,50) + ".....";
                }
                String tname = data.getStr("tname");
                String bvid = data.getStr("bvid");

                Integer view = stat.getInt("view");
                Integer danmaku = stat.getInt("danmaku");
                Integer like = stat.getInt("like");
                Integer favorite = stat.getInt("favorite");
                Integer coin = stat.getInt("coin");
                Integer share = stat.getInt("share");

                String name = owner.getStr("name");

                byte[] bytes = HTTP_CLIENT.newCall(new Request.Builder().url(pic).get().build()).execute().body().byteStream().readAllBytes();
                ExternalResource ex = ExternalResource.Companion.create(bytes);
                Image img = ExternalResource.uploadAsImage(ex,contact);
                MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
                MessageChain messages = messageChainBuilder.append("标题:").append(title).append("\n")
                        .append(img).append("\n")
                        .append("时长:").append(Math.round(duration / 60) + "min").append("\n")
                        .append("视频地址:").append("https://www.bilibili.com/video/" + bvid).append("\n")
                        .append("播放量:").append(String.valueOf(view)).append(" ").append("弹幕:").append(String.valueOf(danmaku)).append(" ")
                        .append("点赞:").append(String.valueOf(like)).append(" ").append("收藏:").append(String.valueOf(favorite)).append(" ")
                        .append("投币:").append(String.valueOf(coin)).append(" ").append("分享:").append(String.valueOf(share)).append("\n")
                        .append("分区:").append(tname).append("\n")
                        .append("up主:").append(name).append("\n")
                        .append("简介:").append(desc).build();
                ex.close();
                return messages;
            }else {
                return null;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Message getV2TopicInfo(String message){
        message = message.substring(message.indexOf("/t/")+3);
        if (message.contains("#")){
            message = message.substring(0,message.indexOf("#"));
        }
        try {
            //TODO 代理地址加入配置文件
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 7890));
            OkHttpClient okHttpClient = new OkHttpClient.Builder().proxy(proxy).build();
            Request request = new Request.Builder().url(V2EX_API + message)
                    .get().header("Authorization","Bearer " + v2Token).build();
            Response response = okHttpClient.newCall(request).execute();
            String respStr = response.body().string();
            JSONObject result = JSONUtil.parseObj(respStr).getJSONObject("result");
            String title = result.getStr("title");
            String content = result.getStr("content");
            String url = result.getStr("url");
            String replies = result.getStr("replies");
            Long created = result.getLong("created");
            String nodeName = result.getJSONObject("node").getStr("title");
            if (content.length()>100){
                content = content.substring(0,80) + ".....";
            }
            MessageChainBuilder messageChainBuilder = new MessageChainBuilder();
            messageChainBuilder.append("标题：").append(title).append("\n").append("\n")
                    .append("内容：").append(content).append("\n").append("\n")
                    .append("回复：").append(replies).append("  ")
                    .append("节点：").append(nodeName).append("  ")
                    .append("创建时间：").append(DateUtil.format(new Date(created),"yyyy-MM-dd")).append("\n").append("\n")
                    .append("链接：").append(url);
            return messageChainBuilder.build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String getBAIntro(String level){
        try {
            String href = baCache.get(level);
            if (href!=null){
                return href;
            }
            //refresh cache
//            Request request = new Request.Builder().url(BA_API).get().build();
//            Response response = HTTP_CLIENT.newCall(request).execute();
//            String respStr = response.body().string();
            InputStream stream = ApiUtil.class.getClassLoader().getResourceAsStream("ba.html");
            String respStr = new String(stream.readAllBytes());
            Document document = Jsoup.parse(respStr);
            Element body = document.body();
            baCache = body.getElementsByAttribute("data-v-72e1856c").stream().filter(element -> {
                if (!element.hasAttr("href")) return false;
                if (!element.hasAttr("class") || !element.attr("class").equals("item")) return false;
                if (!element.hasAttr("title") || !element.attr("title").contains("-")) return false;
                return true;
            }).collect(Collectors.toMap((element -> element.attr("title")), (element -> element.attr("href"))));
            href = baCache.get(level);
            if (href ==null){
                throw new RuntimeException("未找到关卡");
            }else{
                return href;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
