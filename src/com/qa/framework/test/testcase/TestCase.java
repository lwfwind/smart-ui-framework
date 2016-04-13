package com.qa.framework.test.testcase;

import com.qa.framework.TestCaseBase;
import com.qa.framework.ioc.annotation.AutoInject;
import com.qa.framework.test.service.StudentService;
import com.qa.framework.test.service.StudentService_Annotation;
import org.apache.log4j.Logger;
import org.testng.annotations.Test;

/**
 * Created by kcgw001 on 2016/4/12.
 */
public class TestCase extends TestCaseBase {
    @AutoInject
    public StudentService studentService;

    @AutoInject
    public StudentService_Annotation studentService_Annotation;
    protected Logger logger = Logger.getLogger(this.getClass());

    @Test
    public void doTestCase() {
        logger.info("TestCase Level");
        studentService.login();
    }

    @Test
    public void doTestCase_Annotation() {
        logger.info("TestCase_Annotation Level");
        studentService_Annotation.login_Annotation();
    }

    public boolean isUnitTest() {
        return true;
    }
}
