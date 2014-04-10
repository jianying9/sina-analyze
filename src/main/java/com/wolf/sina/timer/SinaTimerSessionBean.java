package com.wolf.sina.timer;

import com.wolf.framework.timer.AbstractTimer;
import com.wolf.sina.config.ActionNames;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.Schedule;
import javax.ejb.Startup;
import javax.ejb.Singleton;

/**
 *
 * @author jianying9
 */
@Singleton
@Startup
public class SinaTimerSessionBean extends AbstractTimer implements SinaTimerSessionBeanLocal {

    @Schedule(minute = "0", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "0,6,12,18", dayOfWeek = "*", persistent = false)
    @Override
    public void refreshSpiderUserCookie() {
        System.out.println("timer:refreshSpiderUserCookie----刷新爬虫:UPDATE_OLDEST_SINA_USER:init");
        Map<String, String> parameterMap = new HashMap<String, String>(2, 1);
        parameterMap.put("operate", "init");
        String result = this.executeService(ActionNames.UPDATE_OLDEST_SINA_USER, parameterMap);
        System.out.println(result);
    }

    @Schedule(minute = "*", second = "0", dayOfMonth = "*", month = "*", year = "*", hour = "*", dayOfWeek = "*", persistent = false)
    @Override
    public void checkUpdateSinaUserTask() {
        System.out.println("timer:checkUpdateSinaUserTask----检查sina用户抓取任务是否运行:UPDATE_OLDEST_SINA_USER:check");
        Map<String, String> parameterMap = new HashMap<String, String>(2, 1);
        parameterMap.put("operate", "check");
        String result = this.executeService(ActionNames.UPDATE_OLDEST_SINA_USER, parameterMap);
        System.out.println(result);
    }
}
