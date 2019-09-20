package com.ui.automation.framework.testng.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.ISuite;
import org.testng.ISuiteListener;

@Slf4j
public class SuiteListener implements ISuiteListener {

    @Override
    public void onStart(ISuite iSuite) {
        log.info(iSuite.getName() + " Start");

        Object obj = ListenerHelper.findImplementClass(ICustomTestListener.class);
        if (obj != null) {
            ICustomTestListener testListenerImp = null;
            testListenerImp = (ICustomTestListener) obj;
            testListenerImp.onStart(iSuite);
        }
    }

    @Override
    public void onFinish(ISuite iSuite) {
        log.info(iSuite.getName() + " Finish");

        Object obj = ListenerHelper.findImplementClass(ICustomTestListener.class);
        if (obj != null) {
            ICustomTestListener testListenerImp = null;
            testListenerImp = (ICustomTestListener) obj;
            testListenerImp.onFinish(iSuite);
        }
    }
}


