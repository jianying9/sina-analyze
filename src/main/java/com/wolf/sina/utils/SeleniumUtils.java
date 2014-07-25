package com.wolf.sina.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author aladdin
 */
public class SeleniumUtils {

    private static String bodyXpath = "/html/body";

    public static void waitTime(long timeOutInSeconds) {
        try {
            Thread.sleep(timeOutInSeconds);
        } catch (InterruptedException ex) {
        }
    }

    public static void waitUrlChange(String oldUrl, WebDriver webDriver, long timeOutInSeconds) {
        System.out.println("判断页面是否跳转...");
        String newUrl = webDriver.getCurrentUrl();
        long times = 0;
        System.out.println("times:" + times);
        System.out.println("oldUrl:" + oldUrl);
        while (newUrl.equals(oldUrl) && times < 5) {
            System.out.println("newUrl:" + newUrl);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ex) {
            }
            newUrl = webDriver.getCurrentUrl();
            times++;
        }
        System.out.println("newUrl:" + newUrl);
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, timeOutInSeconds);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(bodyXpath)));
    }

    public static void waitUntilReady(WebDriver webDriver, String xPath, long timeOutInSeconds) {
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, timeOutInSeconds);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(xPath)));
    }

    public static Map<String, String> parseCookie(String cookies) {
        String[] cookieArr = cookies.split("; ");
        Map<String, String> cookieMap = new HashMap<String, String>(cookieArr.length, 1);
        String[] arr;
        for (String cookie : cookieArr) {
            arr = cookie.split("=");
            cookieMap.put(arr[0], arr[1]);
        }
        return cookieMap;
    }

    public static String createCookie(Map<String, String> cookieMap) {
        StringBuilder cookieBuilder = new StringBuilder(512);
        Set<Map.Entry<String, String>> entrySet = cookieMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            cookieBuilder.append(entry.getKey()).append('=').append(entry.getValue()).append("; ");
        }
        String cookie = cookieBuilder.toString();
        return cookie;
    }
}
