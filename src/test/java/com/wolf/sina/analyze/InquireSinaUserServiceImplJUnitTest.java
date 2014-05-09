package com.wolf.sina.analyze;

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
public class InquireSinaUserServiceImplJUnitTest extends AbstractSinaAnalyzeTest {

    public InquireSinaUserServiceImplJUnitTest() {
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
        parameterMap.put("pageSize", "10");
        String result = this.testHandler.execute(ActionNames.INQUIRE_SINA_USER, parameterMap);
        System.out.println(result);
    }
}