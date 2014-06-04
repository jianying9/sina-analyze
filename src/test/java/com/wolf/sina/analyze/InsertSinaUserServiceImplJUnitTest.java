package com.wolf.sina.analyze;

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
public class InsertSinaUserServiceImplJUnitTest extends AbstractSinaAnalyzeTest {

    public InsertSinaUserServiceImplJUnitTest() {
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
//        parameterMap.put("userId", "2334235882");
        parameterMap.put("userId", "2614553842");
        Response response = this.testHandler.execute(ActionNames.INSERT_SINA_USER, parameterMap);
        System.out.println(response.getResponseMessage());
    }
}