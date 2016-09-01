package com.qa.framework.testnglistener;

import org.testng.ITestContext;
import org.testng.ITestResult;

public interface ICustomTestListener {

    /**
     * On test failure.
     *
     * @param tr the tr
     */
    void onTestFailure(ITestResult tr);

    /**
     * On test skipped.
     *
     * @param tr the tr
     */
    void onTestSkipped(ITestResult tr);

    /**
     * On test success.
     *
     * @param tr the tr
     */
    void onTestSuccess(ITestResult tr);

    void onTestStart(ITestResult tr);

    void onStart(ITestContext testContext);

    void onFinish(ITestContext testContext);
}
