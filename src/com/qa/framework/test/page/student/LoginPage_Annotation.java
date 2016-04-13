package com.qa.framework.test.page.student;

import com.qa.framework.PageBase;
import com.qa.framework.ioc.annotation.Page;
import org.apache.log4j.Logger;

/**
 * Created by apple on 15/9/9.
 */
@Page
public class LoginPage_Annotation {

    protected Logger logger = Logger.getLogger(this.getClass());

    public void login() {
        logger.info("Page Annotation Level");
    }


}
