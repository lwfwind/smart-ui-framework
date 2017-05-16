package com.qa.framework.testnglistener;

import org.apache.log4j.Logger;
import org.testng.*;

import static com.qa.framework.ioc.IocHelper.findImplementClass;


public class SuiteListener implements ISuiteListener {
    private static Logger logger = Logger.getLogger(SuiteListener.class);

    @Override
    public void onStart(ISuite iSuite) {
        logger.info(iSuite.getName() + " Start");

        Class<?> clazz = findImplementClass(ICustomTestListener.class);
        if (clazz != null) {
            ICustomTestListener testListenerImp = null;
            try {
                testListenerImp = (ICustomTestListener) clazz.newInstance();
                testListenerImp.onStart(iSuite);
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void onFinish(ISuite iSuite) {
        logger.info(iSuite.getName() + " Finish");

        Class<?> clazz = findImplementClass(ICustomTestListener.class);
        if (clazz != null) {
            ICustomTestListener testListenerImp = null;
            try {
                testListenerImp = (ICustomTestListener) clazz.newInstance();
                testListenerImp.onFinish(iSuite);
            } catch (InstantiationException | IllegalAccessException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }
}


