package com.wolf.sina.spider.localservice;

import com.wolf.framework.dao.REntityDao;
import com.wolf.framework.dao.annotation.InjectRDao;
import com.wolf.framework.dao.condition.InquirePageContext;
import com.wolf.framework.local.LocalServiceConfig;
import com.wolf.sina.spider.entity.SpiderUserEntity;
import com.wolf.sina.utils.SeleniumUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

/**
 *
 * @author aladdin
 */
@LocalServiceConfig(
        interfaceInfo = SpiderLocalService.class,
        description = "爬虫相关接口")
public class SipderLocalServiceImpl implements SpiderLocalService {

    @InjectRDao(clazz = SpiderUserEntity.class)
    private REntityDao<SpiderUserEntity> spiderUserEntityDao;
    //http client
    private volatile HttpClientManager httpClientManager;

    @Override
    public void init() {
        //初始化http client
    }

    @Override
    public void insertSpiderUser(String userName, String password) {
        Map<String, String> insertMap = new HashMap<String, String>(2, 1);
        insertMap.put("userName", userName);
        insertMap.put("password", password);
        insertMap.put("cookie", "");
        insertMap.put("lastUpdateTime", "1");
        this.spiderUserEntityDao.insert(insertMap);
    }
    
    @Override
    public void updateSpiderUser(Map<String, String> updateMap) {
        this.spiderUserEntityDao.update(updateMap);
    }

    @Override
    public List<SpiderUserEntity> inquireSpiderUser() {
        InquirePageContext inquirePageContext = new InquirePageContext();
        inquirePageContext.setPageSize(100);
        return this.spiderUserEntityDao.inquire(inquirePageContext);
    }

    @Override
    public void deleteSpiderUser(String userName) {
        this.spiderUserEntityDao.delete(userName);
    }
    private final String loginUrl = "http://weibo.com/";
    private final String userNameXpath = "//*[@id=\"pl_login_form\"]/div[5]/div[1]/div/input";
    private final String passwordXpath = "//*[@id=\"pl_login_form\"]/div[5]/div[2]/div/input";
    private final String loginBtnXpath = "//*[@id=\"pl_login_form\"]/div[5]/div[6]/div[1]/a";

    @Override
    public String getCookieByLogin(String userName, String password) {
        FirefoxProfile fp = new FirefoxProfile();
        WebDriver webDriver = new FirefoxDriver(fp);
        //清除cookie
        webDriver.get(this.loginUrl);
        SeleniumUtils.waitUntilReady(webDriver, 60);
        webDriver.manage().deleteAllCookies();
        //刷新
        webDriver.get(this.loginUrl);
        SeleniumUtils.waitUntilReady(webDriver, 60);
        //输入帐号密码
        WebElement userNameElement = webDriver.findElement(By.xpath(this.userNameXpath));
        userNameElement.sendKeys(userName);
        WebElement passwordElement = webDriver.findElement(By.xpath(this.passwordXpath));
        passwordElement.sendKeys(password);
        //登录
        WebElement loginBtnElement = webDriver.findElement(By.xpath(this.loginBtnXpath));
        loginBtnElement.click();
        //等待页面跳转
        SeleniumUtils.waitUrlChange(this.loginUrl, webDriver, 60);
        Set<Cookie> allCookies = webDriver.manage().getCookies();
        Map<String, String> loginCookieMap = new HashMap<String, String>(16, 1);
        for (Cookie cookie : allCookies) {
            loginCookieMap.put(cookie.getName(), cookie.getValue());
        }
        String result = SeleniumUtils.createCookie(loginCookieMap);
        return result;
    }
}
