package com.qa.framework.testnglistener;

import org.testng.ISuite;
import org.testng.ITestContext;
import org.testng.ITestResult;

/**
 * The interface Custom test listener.
 */
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

    /**
     * On test start.
     *
     * @param tr the tr
     */
    void onTestStart(ITestResult tr);

    /**
     * On start.
     *
     * @param testContext the test context
     */
    void onStart(ITestContext testContext);

    void onStart(ISuite iSuite);

    /**
     * On finish.
     *
     * @param iSuite the test context
     */
    void onFinish(ISuite iSuite);

    void onFinish(ITestContext testContext);
}
