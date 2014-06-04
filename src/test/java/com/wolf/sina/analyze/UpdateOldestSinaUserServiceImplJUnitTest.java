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
public class UpdateOldestSinaUserServiceImplJUnitTest extends AbstractSinaAnalyzeTest {

    public UpdateOldestSinaUserServiceImplJUnitTest() {
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
        parameterMap.put("operate", "test");
        Response response = this.testHandler.execute(ActionNames.UPDATE_OLDEST_SINA_USER, parameterMap);
        System.out.println(response.getResponseMessage());
    }
}