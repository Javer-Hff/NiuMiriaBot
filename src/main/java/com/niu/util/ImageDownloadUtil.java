package com.niu.util;

import cn.hutool.json.JSONUtil;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 60s新闻速览图片下载工具
 * @authoer:hff
 * @Date 2023/8/2 16:29
 */
public class ImageDownloadUtil {
    private static final String NEWS_API = "http://dwz.2xb.cn/zaob";

    private static final String MOYU_API ="https://api.vvhan.com/api/moyu?type=json";

    public static boolean downloadNews(){
        try{
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(NEWS_API).get().build();
            Response response = okHttpClient.newCall(request).execute();
            String body = response.body().string();
            if (JSONUtil.parseObj(body).getInt("code")!=200){
                return false;
            }
            String imageUrl = JSONUtil.parseObj(body).get("imageUrl").toString();
            downloadImage("60s.jpg",imageUrl);
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static boolean downloadMoyu(){
        try{
            OkHttpClient okHttpClient = new OkHttpClient();
            Request request = new Request.Builder().url(MOYU_API).get().build();
            Response response = okHttpClient.newCall(request).execute();
            String body = response.body().string();
            if (!JSONUtil.parseObj(body).getBool("success")){
                return false;
            }
            String imageUrl = JSONUtil.parseObj(body).get("url").toString();
            downloadImage("moyu.jpg",imageUrl);
            return true;
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    private static void downloadImage(String fileName, String downloadUrl) throws IOException {
        File file = new File("./" + fileName);
        file.deleteOnExit();
        InputStream byteStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            byteStream = okHttpClient.newCall(new Request.Builder().url(downloadUrl).get().build()).execute().body().byteStream();
            fileOutputStream = new FileOutputStream("./" + fileName);
            byte[] buffer = new byte[2048];
            int len = 0;
            while ((len = byteStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, len);
            }
            fileOutputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            fileOutputStream.close();
            byteStream.close();
        }
    }
}
