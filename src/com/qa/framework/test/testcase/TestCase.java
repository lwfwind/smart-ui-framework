package com.qa.framework.test.testcase;

import com.qa.framework.TestCaseBase;
import com.qa.framework.ioc.annotation.AutoInject;
import com.qa.framework.test.service.StudentService;
import org.testng.annotations.Test;

/**
 * Created by kcgw001 on 2016/4/12.
 */
public class TestCase extends TestCaseBase {
    @AutoInject
    public StudentService studentService;

    @Test
    public void doTestCase() {
        logger.info("TestCase Level");
        studentService.login();
    }

    public boolean isUnitTest() {
        return true;
    }
}
