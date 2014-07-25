package com.wolf.sina.spider;

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
public class InsertSpiderUserServiceImplJUnitTest extends AbstractSinaAnalyzeTest {

    public InsertSpiderUserServiceImplJUnitTest() {
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
        Map<String, String> parameterMap = new HashMap<String, String>(2, 1);
        parameterMap.put("userName", "sina_20140016@163.com");
        parameterMap.put("password", "sina1024");
        Response response = this.testHandler.execute(ActionNames.INSERT_SPIDER_USER, parameterMap);
        System.out.println(response.getResponseMessage());
    }
}