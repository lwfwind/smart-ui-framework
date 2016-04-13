package com.qa.framework.test.service;

import com.qa.framework.ioc.annotation.AutoInject;
import com.qa.framework.ioc.annotation.Service;
import com.qa.framework.test.page.student.LoginPage;
import com.qa.framework.test.page.student.LoginPage_Annotation;
import org.apache.log4j.Logger;

/**
 * Created by kcgw001 on 2016/3/25.
 */
@Service
public class StudentService_Annotation {
    protected Logger logger = Logger.getLogger(this.getClass());
    @AutoInject
    protected LoginPage loginPage;

    @AutoInject
    protected LoginPage_Annotation loginPage_Annotation;

    public void login() {
        logger.info("Service Level");
        loginPage.login();
    }

    public void login_Annotation() {
        logger.info("Service Annotation Level");
        loginPage_Annotation.login();
    }

}
