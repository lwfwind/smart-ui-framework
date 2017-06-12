package com.qa.framework.testnglistener;

import com.qa.framework.config.PropConfig;
import org.apache.log4j.Logger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

/**
 * TestNG retry Analyzer.
 */
public class TestngRetry implements IRetryAnalyzer {
    private static Logger logger = Logger.getLogger(TestngRetry.class);
    private static int maxRetryCount;

    static {
        maxRetryCount = PropConfig.getRetryCount();
        logger.info("retrycount=" + maxRetryCount);
    }

    private int retryCount = 1;

    public boolean retry(ITestResult result) {
        if (retryCount <= maxRetryCount) {
            String message = "Retry for [" + result.getName() + "] on class [" + result.getTestClass().getName() + "] Retry "
                    + retryCount + " times";
            logger.info(message);
            Reporter.setCurrentTestResult(result);
            Reporter.log("RunCount=" + (retryCount + 1));
            retryCount++;
            return true;
        }
        return false;
    }

}
