package com.wolf.sina.spider.localservice;

import com.wolf.framework.dao.REntityDao;
import com.wolf.framework.dao.annotation.InjectRDao;
import com.wolf.framework.dao.condition.InquirePageContext;
import com.wolf.framework.local.LocalServiceConfig;
import com.wolf.sina.spider.entity.SpiderUserEntity;
import com.wolf.sina.utils.SeleniumUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.Header;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.client.params.CookiePolicy;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
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
    //登录页面
    private final String loginUrl = "http://login.sina.com.cn/";
    private final String weiboUrl = "http://weibo.com/";
    private final String userNameXpath = "//*[@id=\"username\"]";
    private final String passwordXpath = "//*[@id=\"password\"]";
    private final String loginBtnXpath = "//*[@id=\"main_login\"]/div[2]/div[2]/div/form/div/ul/li[7]/a[1]/input";
    //
    private final Pattern filterPattern = Pattern.compile("\\\\t|\\\\n|\\\\r|\\\\");
    //
    //获取用户详细信息
    private final String infoPath = "${id}/info?from=page_100505&mod=TAB&ajaxpagelet=1";
    private final Pattern replaceIdPattern = Pattern.compile("\\$\\{id\\}");
    private final Pattern infoValuePattern = Pattern.compile("(?:\"con\">)([\\w\\W]*?)(?:<)");
    private final Pattern infoLablePattern = Pattern.compile("(?:\"label S_txt2\">)([\\w\\W]*?)(?:<)");
    private final Pattern infoTagPattern = Pattern.compile("(?:\"tag\">)([\\w\\W]*?)(?:<)");
    //获取粉丝信息
    private final String followPath = "${id}/follow?ajaxpagelet=1&page=${page}";
    private final Pattern replacePagePattern = Pattern.compile("\\$\\{page\\}");
    private final Pattern followPattern = Pattern.compile("(?:\\&uid=)(\\d*)");

    @Override
    public void init() {
        //初始化http client
        this.rebuildHttpClientManager();
    }

    @Override
    public void rebuildHttpClientManager() {
        PoolingClientConnectionManager cm;
        DefaultHttpClient httpClient;
        Header header;
        HttpClientManager hcm = new HttpClientManager();
        List<SpiderUserEntity> spiderUserEntityList = this.inquireSpiderUser();
        for (SpiderUserEntity spiderUserEntity : spiderUserEntityList) {
            if (spiderUserEntity.getCookie().isEmpty() == false) {
                System.out.println("有效的帐号:" + spiderUserEntity.getUserName());
                cm = new PoolingClientConnectionManager();
                cm.setMaxTotal(10);
                httpClient = new DefaultHttpClient(cm);
                List<Header> headerList = new ArrayList<Header>(10);
                header = new BasicHeader("Cookie", spiderUserEntity.getCookie());
                headerList.add(header);
                httpClient.getParams().setParameter(ClientPNames.DEFAULT_HEADERS, headerList);
                httpClient.getParams().setParameter(ClientPNames.COOKIE_POLICY, CookiePolicy.IGNORE_COOKIES);
                hcm.add(httpClient);
            }
        }
        this.httpClientManager = hcm;
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

    @Override
    public String getCookieByLogin(String userName, String password) {
        System.out.println(userName + ":开始刷新cookie");
        FirefoxProfile fp = new FirefoxProfile();
        WebDriver webDriver = new FirefoxDriver(fp);
        //清除cookie
//        System.out.println(userName + ":清除cookie");
//        webDriver.get(this.rootUrl);
//        SeleniumUtils.waitUntilReady(webDriver, 60);
//        webDriver.manage().deleteAllCookies();
        //刷新
        System.out.println(userName + ":打开登录页面:" + this.loginUrl);
        webDriver.get(this.loginUrl);
        SeleniumUtils.waitUntilReady(webDriver, this.userNameXpath, 120);
        System.out.println(userName + ":输入帐号");
        //输入帐号密码
        WebElement userNameElement = webDriver.findElement(By.xpath(this.userNameXpath));
        userNameElement.sendKeys(userName);
        System.out.println(password + ":输入密码");
        WebElement passwordElement = webDriver.findElement(By.xpath(this.passwordXpath));
        passwordElement.sendKeys(password);
        //登录
        System.out.println(userName + ":点击登录按钮");
        WebElement loginBtnElement = webDriver.findElement(By.xpath(this.loginBtnXpath));
        loginBtnElement.click();
        //等待登录页面跳转
        System.out.println(userName + ":等待登录成功页面跳转");
        SeleniumUtils.waitUrlChange(this.loginUrl, webDriver, 120);
        //跳转到weibo页面
        System.out.println(userName + ":跳转到weibo页面");
        webDriver.get(this.weiboUrl);
        System.out.println(userName + ":等待weibo页面跳转");
        SeleniumUtils.waitUrlChange(this.weiboUrl, webDriver, 120);
        System.out.println(userName + ":登录成功获取cookie");
        Set<Cookie> allCookies = webDriver.manage().getCookies();
        Map<String, String> loginCookieMap = new HashMap<String, String>(16, 1);
        for (Cookie cookie : allCookies) {
            loginCookieMap.put(cookie.getName(), cookie.getValue());
        }
        String result = SeleniumUtils.createCookie(loginCookieMap);
        webDriver.close();
        //验证cookie是否有效
        if (result.indexOf("SUE=") == -1) {
            System.out.println(userName + ":获取cookie无效");
            result = "";
        } else {
            System.out.println(userName + ":获取cookie成功");
        }
        return result;
    }

    private String getUrl(String url) {
        System.out.println(url);
        String responseBody = "";
        DefaultHttpClient client = this.httpClientManager.getClient();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        HttpGet httpGet = new HttpGet(url);
        HttpConnectionParams.setConnectionTimeout(httpGet.getParams(), 20000);
        HttpConnectionParams.setSoTimeout(httpGet.getParams(), 20000);
        try {
            responseBody = client.execute(httpGet, responseHandler);
        } catch (IOException ex) {
        }
        return responseBody;
    }

    @Override
    public InfoEntity getInfo(String userId) {
        InfoEntity infoEntity = null;
        String path = this.replaceIdPattern.matcher(this.infoPath).replaceFirst(userId);
        String url = this.weiboUrl.concat(path);
        String response = this.getUrl(url);
        int index = response.lastIndexOf("基本信息");
        if (index > -1) {
            infoEntity = new InfoEntity(userId);
            response = response.substring(index);
            String infoBase = response;
            index = infoBase.indexOf("</script>");
            infoBase = infoBase.substring(0, index);
            infoBase = this.filterPattern.matcher(infoBase).replaceAll("");
            //获取label
            Matcher infoLabelMatcher = this.infoLablePattern.matcher(infoBase);
            List<String> labelList = new ArrayList<String>(8);
            while (infoLabelMatcher.find()) {
                labelList.add(infoLabelMatcher.group(1));
            }
            //获取label value
            Matcher infoValueMatcher = this.infoValuePattern.matcher(infoBase);
            List<String> valueList = new ArrayList<String>(8);
            while (infoValueMatcher.find()) {
                valueList.add(infoValueMatcher.group(1));
            }
            //获取用户信息标值
            String name;
            String value;
            for (int num = 0; num < labelList.size() && num < valueList.size(); num++) {
                name = labelList.get(num);
                value = valueList.get(num);
                if (name.length() > 0) {
                    if (name.equals("昵称")) {
                        infoEntity.setNickName(value);
                    } else if (name.equals("所在地")) {
                        infoEntity.setLocation(value);
                    } else if (name.equals("性别")) {
                        infoEntity.setGender(value);
                    } else if (name.equals("真实姓名")) {
                        infoEntity.setEmpName(value);
                    }
                }
            }
            //获取标签
            index = response.lastIndexOf("标签信息");
            if (index > -1) {
                String infoTag = response.substring(index);
                index = infoTag.indexOf("</script>");
                infoTag = infoTag.substring(0, index);
                infoTag = this.filterPattern.matcher(infoTag).replaceAll("");
                Matcher infoTagMatcher = this.infoTagPattern.matcher(infoTag);
                StringBuilder tagBuilder = new StringBuilder(256);
                while (infoTagMatcher.find()) {
                    value = infoTagMatcher.group(1);
                    tagBuilder.append(value.toLowerCase()).append(',');
                }
                if (tagBuilder.length() > 0) {
                    tagBuilder.setLength(tagBuilder.length() - 1);
                }
                String tag = tagBuilder.toString();
                infoEntity.setTag(tag);
            }
        }
        return infoEntity;
    }

    @Override
    public List<String> getFollow(String userId) {
        Set<String> followSet = new HashSet<String>(64, 1);
        String path = this.replaceIdPattern.matcher(this.followPath).replaceFirst(userId);
        String url = this.weiboUrl.concat(path);
        String response;
        String pageUrl;
        int index;
        String followText;
        Matcher followMatcher;
        String id;
        boolean hasNext = true;
        for (int page = 1; page <= 10 && hasNext; page++) {
            pageUrl = this.replacePagePattern.matcher(url).replaceFirst(Integer.toString(page));
            response = this.getUrl(pageUrl);
            index = response.indexOf("pl.content.followTab.index");
            if (index > -1) {
                followText = response.substring(index);
                index = followText.indexOf("</script>");
                followText = followText.substring(0, index);
                followText = this.filterPattern.matcher(followText).replaceAll("");
                followMatcher = this.followPattern.matcher(followText);
                while (followMatcher.find()) {
                    id = followMatcher.group(1);
                    if (followSet.contains(id)) {
                        hasNext = false;
                        break;
                    } else {
                        followSet.add(id);
                    }
                }
            } else {
                break;
            }
            if (followSet.isEmpty()) {
                break;
            }
        }
        List<String> followList = new ArrayList<String>(followSet.size());
        followList.addAll(followSet);
        return followList;
    }
}
