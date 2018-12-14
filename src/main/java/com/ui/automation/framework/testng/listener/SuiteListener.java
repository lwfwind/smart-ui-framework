package com.ui.automation.framework.testng.listener;

import org.apache.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;


public class SuiteListener implements ISuiteListener {
    private static Logger logger = Logger.getLogger(SuiteListener.class);

    @Override
    public void onStart(ISuite iSuite) {
        logger.info(iSuite.getName() + " Start");

        Object obj = ListenerHelper.findImplementClass(ICustomTestListener.class);
        if (obj != null) {
            ICustomTestListener testListenerImp = null;
            testListenerImp = (ICustomTestListener) obj;
            testListenerImp.onStart(iSuite);
        }
    }

    @Override
    public void onFinish(ISuite iSuite) {
        logger.info(iSuite.getName() + " Finish");

        Object obj = ListenerHelper.findImplementClass(ICustomTestListener.class);
        if (obj != null) {
            ICustomTestListener testListenerImp = null;
            testListenerImp = (ICustomTestListener) obj;
            testListenerImp.onFinish(iSuite);
        }
    }
}


