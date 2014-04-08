import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author jianying9
 */
public class NewEmptyJUnitTest {

    public NewEmptyJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    private String loginUrl = "http://weibo.com/";
    private String userNameXpath = "//*[@id=\"pl_login_form\"]/div[5]/div[1]/div/input";
    private String passwordXpath = "//*[@id=\"pl_login_form\"]/div[5]/div[2]/div/input";
    private String loginBtnXpath = "//*[@id=\"pl_login_form\"]/div[5]/div[6]/div[1]/a";
    private String waitXpath = "/html/body";

    @Test
    public void hello1() {
        FirefoxProfile fp = new FirefoxProfile();
        WebDriver webDriver = new FirefoxDriver(fp);
        //清除cookie
        WebDriverWait webDriverWait = new WebDriverWait(webDriver, 60);
        webDriver.get(this.loginUrl);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(this.waitXpath)));
        webDriver.manage().deleteAllCookies();
        //刷新
        webDriver.get(this.loginUrl);
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(this.loginBtnXpath)));
        //输入帐号密码
        WebElement userNameElement = webDriver.findElement(By.xpath(this.userNameXpath));
        userNameElement.sendKeys("sina_20140001@163.com");
        WebElement passwordElement = webDriver.findElement(By.xpath(this.passwordXpath));
        passwordElement.sendKeys("sina1024");
        //登录
        WebElement loginBtnElement = webDriver.findElement(By.xpath(this.loginBtnXpath));
        loginBtnElement.click();
        //等待页面跳转
        String newUrl = webDriver.getCurrentUrl();
        while (newUrl.equals(this.loginUrl)) {
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException ex) {
            }
            newUrl = webDriver.getCurrentUrl();
        }
        webDriverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(this.waitXpath)));
        Set<Cookie> allCookies = webDriver.manage().getCookies();
        Map<String, String> loginCookieMap = new HashMap<String, String>(16, 1);
        for (Cookie cookie : allCookies) {
            loginCookieMap.put(cookie.getName(), cookie.getValue());
        }
        System.out.println(loginCookieMap);
    }
}