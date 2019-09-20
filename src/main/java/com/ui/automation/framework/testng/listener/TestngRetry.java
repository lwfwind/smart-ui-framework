package com.ui.automation.framework.testng.listener;

import lombok.extern.slf4j.Slf4j;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import org.testng.Reporter;

/**
 * TestNG retry Analyzer.
 */
@Slf4j
public class TestngRetry implements IRetryAnalyzer {
    private static int maxRetryCount = 1;

    static {
        log.info("retryCount=" + maxRetryCount);
    }

    private int retryCount = 1;

    public boolean retry(ITestResult result) {
        if (retryCount <= maxRetryCount) {
            String message = "Retry for [" + result.getName() + "] on class [" + result.getTestClass().getName() + "] Retry "
                    + retryCount + " times";
            log.info(message);
            Reporter.setCurrentTestResult(result);
            Reporter.log("RunCount=" + (retryCount + 1));
            retryCount++;
            return true;
        }
        return false;
    }

}
