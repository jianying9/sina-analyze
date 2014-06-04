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
public class InquireSinaUserByUserIdServiceImplJUnitTest extends AbstractSinaAnalyzeTest {

    public InquireSinaUserByUserIdServiceImplJUnitTest() {
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
        parameterMap.put("userId", "1020887697");
        Response response = this.testHandler.execute(ActionNames.INQUIRE_SINA_USER_BY_USER_ID, parameterMap);
        System.out.println(response.getResponseMessage());
    }
}