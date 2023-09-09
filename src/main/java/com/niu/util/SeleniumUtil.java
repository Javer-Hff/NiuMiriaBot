package com.niu.util;

import cn.hutool.core.io.FileUtil;
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

import java.io.File;
import java.io.IOException;
import java.time.Duration;

/**
 * TODO
 *
 * @authoer:hff
 * @Date 2023/9/4 14:35
 */
public class SeleniumUtil {
    private static EdgeDriver driver;

    static {
        //配置本地的chromediver.exe谷歌的内核
        System.setProperty("webdriver.edge.driver", "C:\\Users\\wxhld\\Downloads\\edgedriver_win64\\msedgedriver.exe");
        //设置ChromeOptions打开方式，设置headless：不弹出浏览器
        EdgeOptions options = new EdgeOptions();
        options.addArguments("headless");
        options.addArguments("--remote-allow-origins=*");
        //设置好使用ChromeDriver使用
        driver = new EdgeDriver(options);
        driver.manage().timeouts().scriptTimeout(Duration.ofMinutes(1));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(1));
        driver.manage().timeouts().implicitlyWait(Duration.ofMinutes(1));
    }

    public static void screenshot(String message, Contact contact) throws InterruptedException, IOException {
        //获取要截图的地址，注：需要先获取地址哦，不然下方获取的宽度高度就会是弹窗的高和宽，而不是页面内容的高宽
        driver.get(message);

        String script = "return (window.performance.timing.loadEventEnd - window.performance.timing.navigationStart) >= 0;";
        WebDriverWait webDriverWait = new WebDriverWait(driver,Duration.ofSeconds(50));
        webDriverWait.until(ExpectedConditions.jsReturnsValue(script));

        Thread.sleep(5000);
        //获取页面高宽使用：return document.documentElement.scrollWidth
        Long width = (Long) ((JavascriptExecutor)driver).executeScript("return document.documentElement.scrollWidth");
        Long height = (Long) ((JavascriptExecutor)driver).executeScript("return document.documentElement.scrollHeight");
        System.out.println("========================");
        System.out.println(width);
        System.out.println(height);
        System.out.println("========================");
        //设置浏览器弹窗页面的大小
        driver.manage().window().setSize(new Dimension(width.intValue(), height.intValue()));
        //使用getScreenshotAs进行截取屏幕
        File srcFile = driver.getScreenshotAs(OutputType.FILE);
        File targetFile = new File("./Screen.png");
        if (!targetFile.exists()){
            targetFile.createNewFile();
        }
        FileUtil.copy(srcFile, targetFile,true);
        //返回图片
        ExternalResource ex = ExternalResource.Companion.create(FileUtil.readBytes(targetFile));
        Image img = ExternalResource.uploadAsImage(ex,contact);
        MessageChain sendMessage = new MessageChainBuilder().append(img).build();
        contact.sendMessage(sendMessage);

        driver.manage().window().setSize(new Dimension(600,800));
    }
}
