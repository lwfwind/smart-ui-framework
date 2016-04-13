package com.qa.framework.test.service;

import com.qa.framework.ServiceBase;
import com.qa.framework.ioc.annotation.AutoInject;
import com.qa.framework.test.page.student.LoginPage;
import org.apache.log4j.Logger;

/**
 * Created by kcgw001 on 2016/3/25.
 */
public class StudentService extends ServiceBase {
    protected Logger logger = Logger.getLogger(this.getClass());
    @AutoInject
    protected LoginPage loginPage;

    public void login() {
        logger.info("Service Level");
        loginPage.login();
    }

}
