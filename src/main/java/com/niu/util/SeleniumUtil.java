package com.niu.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.net.URLEncodeUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.niu.config.BotConfig;
import jakarta.annotation.PostConstruct;
import net.mamoe.mirai.contact.Contact;
import net.mamoe.mirai.message.data.Image;
import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MessageChainBuilder;
import net.mamoe.mirai.utils.ExternalResource;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Set;

/**
 * TODO
 *
 * @authoer:hff
 * @Date 2023/9/4 14:35
 */
@Component
public class SeleniumUtil {
    private static EdgeDriver driver;

    private static final String TENCENT_URL_SEC = "https://api.kit9.cn/api/360_security/api.php?url=";

    @Autowired
    public BotConfig botConfig;

    @PostConstruct
    private void init(){
        //配置本地的msedgedriver.exe的edge浏览器内核
        System.setProperty("webdriver.edge.driver", botConfig.getWorkdir() + "msedgedriver.exe");
        //设置EdgeOptions打开方式，设置headless：无头模式(不弹出浏览器)
        EdgeOptions options = new EdgeOptions();
        options.addArguments("headless");
        options.addArguments("--remote-allow-origins=*");

        driver = new EdgeDriver(options);
        driver.manage().timeouts().scriptTimeout(Duration.ofMinutes(1));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(1));
        driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(1));
    }

    public static InputStream screenshot(String message) throws InterruptedException, IOException {
        if (!urlsec(message)){
            return null;
        }
        //获取要截图的地址，注：需要先获取地址哦，不然下方获取的宽度高度就会是弹窗的高和宽，而不是页面内容的高宽
        driver.get(message);

        String script = "return (window.performance.timing.loadEventEnd - window.performance.timing.navigationStart) >= 0;";
        WebDriverWait webDriverWait = new WebDriverWait(driver,Duration.ofSeconds(50));
        webDriverWait.until(ExpectedConditions.jsReturnsValue(script));

        Thread.sleep(5000);
        //获取页面高宽使用：return document.documentElement.scrollWidth
        Long width = (Long) ((JavascriptExecutor)driver).executeScript("return document.documentElement.scrollWidth");
        Long height = (Long) ((JavascriptExecutor)driver).executeScript("return document.documentElement.scrollHeight");

        //设置浏览器弹窗页面的大小
        driver.manage().window().setSize(new Dimension(width.intValue(), height.intValue()));
        //使用getScreenshotAs进行截取屏幕
        byte[] screenshotBytes = driver.getScreenshotAs(OutputType.BYTES);

        //打开新的标签页关闭当前标签页
        String currentWindow = driver.getWindowHandle();
        Set<String> windowHandles = driver.getWindowHandles();
        for (String windowHandle : windowHandles) {
            if (!windowHandle.equals(currentWindow)){
                driver.switchTo().window(windowHandle).close();
            }
        }
        driver.switchTo().window(currentWindow);

        //还原窗口大小
        driver.manage().window().setSize(new Dimension(600,800));
        return new ByteArrayInputStream(screenshotBytes);
    }

    //腾讯网址安全中心https://urlsec.qq.com/check.html
    //腾讯域名检测
    public static boolean urlsec(String url){
        String requestUrl = TENCENT_URL_SEC + url;
        try {
            String bodyString = HttpRequestUtil.builder().get(requestUrl).body().string();
            JSONObject data = JSONUtil.parseObj(bodyString).getJSONObject("data");
            Integer state = data.getInt("state");
            if (state==2){
                return false;
            }
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
