package com.wolf.sina.analyze;

import com.wolf.framework.utils.TimeUtils;
import com.wolf.framework.worker.context.Response;
import com.wolf.sina.AbstractSinaAnalyzeTest;
import com.wolf.sina.config.ActionNames;
import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author aladdin
 */
public class TimerSaveSinaUserNumServiceImplJUnitTest extends AbstractSinaAnalyzeTest {

    public TimerSaveSinaUserNumServiceImplJUnitTest() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    //

    @Test
    public void test() {
        String time = TimeUtils.getDateFotmatYYMMDDHHmmSS();
        time = time.substring(0, 13);
        Map<String, String> parameterMap = new HashMap<String, String>(2, 1);
        parameterMap.put("time", time);
        Response response = this.testHandler.execute(ActionNames.TIMER_SAVE_SINA_USER_NUM, parameterMap);
        System.out.println(response.getResponseMessage());
    }
}