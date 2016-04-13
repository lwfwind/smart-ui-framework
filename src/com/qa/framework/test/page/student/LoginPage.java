package com.qa.framework.test.page.student;

import com.qa.framework.PageBase;
import org.apache.log4j.Logger;

/**
 * Created by apple on 15/9/9.
 */
public class LoginPage extends PageBase {

    protected Logger logger = Logger.getLogger(this.getClass());

    public void login() {
        logger.info("Page Level");
    }


}
