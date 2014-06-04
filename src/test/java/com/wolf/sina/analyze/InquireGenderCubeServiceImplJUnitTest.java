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
public class InquireGenderCubeServiceImplJUnitTest extends AbstractSinaAnalyzeTest {

    public InquireGenderCubeServiceImplJUnitTest() {
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
        Response response = this.testHandler.execute(ActionNames.INQUIRE_GENDER_CUBE, parameterMap);
        System.out.println(response.getResponseMessage());
    }
}