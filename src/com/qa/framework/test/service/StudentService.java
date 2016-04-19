package com.qa.framework.test.service;

import com.qa.framework.ServiceBase;
import com.qa.framework.ioc.annotation.AutoInject;
import com.qa.framework.test.page.student.LoginPage;

/**
 * Created by kcgw001 on 2016/3/25.
 */
public class StudentService extends ServiceBase {
    @AutoInject
    protected LoginPage loginPage;

    public void login() {
        logger.info("Service Level");
        loginPage.login();
    }

}
