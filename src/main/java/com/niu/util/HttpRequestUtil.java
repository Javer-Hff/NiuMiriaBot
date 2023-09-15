package com.niu.util;

import cn.hutool.json.JSONUtil;
import jakarta.annotation.PostConstruct;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * http请求工具类
 * @authoer:hff
 * @Date 2023/9/15 9:34
 */
@Component
public class HttpRequestUtil {
    //连接超时时间 秒
    private static final long CONN_TIMEOUT = 10;
    //读写超时时间 秒
    private static final long TIMEOUT = 10;
    private static OkHttpClient client;
    //代理IP
    private static String proxyHost;
    //代理端口
    private static Integer proxyPort;
    //请求参数
    private Map<String, String> paramMap;
    //请求头
    private Map<String, String> headerMap;
    @Value("${bot.proxy.host}")
    public void setProxyHost(String proxyHost){
        HttpRequestUtil.proxyHost = proxyHost;
    }

    @Value("${bot.proxy.port}")
    public void setProxyPort(int proxyPort){
        HttpRequestUtil.proxyPort = proxyPort;
    }


    @PostConstruct
    private void init(){
        if (client == null) {
            synchronized (HttpRequestUtil.class) {
                if (client == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .connectTimeout(CONN_TIMEOUT, TimeUnit.SECONDS)
                            .writeTimeout(TIMEOUT, TimeUnit.SECONDS)
                            .readTimeout(TIMEOUT, TimeUnit.SECONDS);
                    if (proxyHost != null && proxyPort != null) {
                        builder.proxy(new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxyHost, proxyPort)));
                    }
                    client = builder.build();
                }
            }
        }
    }

    private HttpRequestUtil() {
    }

    public static HttpRequestUtil builder() {
        return new HttpRequestUtil();
    }

    public HttpRequestUtil setParam(String key,String value){
        if (paramMap==null){
            paramMap = new LinkedHashMap<>();
        }
        paramMap.put(key,value);
        return this;
    }

    public HttpRequestUtil setHeader(String key,String value){
        if (headerMap==null){
            headerMap = new LinkedHashMap<>();
        }
        headerMap.put(key,value);
        return this;
    }

    public Response get(String url){
        Request.Builder builder = new Request.Builder().get();
        StringBuilder urlBuilder = new StringBuilder(url);
        if (paramMap!=null&&paramMap.size()>0){
            urlBuilder.append("?");
            for (Map.Entry<String, String> entry : paramMap.entrySet()) {
                urlBuilder.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8))
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                        .append("&");
            }
            urlBuilder.deleteCharAt(urlBuilder.length()-1);
        }
        if (headerMap!=null&&headerMap.size()>0){
            for (Map.Entry<String, String> entry : headerMap.entrySet()){
                builder.header(entry.getKey(),entry.getValue());
            }
        }
        Request request = builder.url(urlBuilder.toString()).build();
        try {
             return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Response post(String url,boolean isJson){
        RequestBody body = null;
        if (isJson){
            if (paramMap!=null&&paramMap.size()!=0){
                String jsonStr = JSONUtil.toJsonStr(paramMap);
                body = RequestBody.create(jsonStr, MediaType.Companion.parse("application/json; charset=utf-8"));
            }
        }else {
            if (paramMap!=null&&paramMap.size()!=0){
                FormBody.Builder formBuilder = new FormBody.Builder();
                for (Map.Entry<String, String> entry : paramMap.entrySet()){
                    formBuilder.add(entry.getKey(),entry.getValue());
                }
                body = formBuilder.build();
            }
        }
        Request.Builder builder = new Request.Builder().post(body);
        if (headerMap!=null&&headerMap.size()>0){
            for (Map.Entry<String, String> entry : headerMap.entrySet()){
                builder.header(entry.getKey(),entry.getValue());
            }
        }
        Request request = builder.url(url).build();
        try {
            return client.newCall(request).execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }



}
