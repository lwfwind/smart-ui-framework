package com.qa.framework.testnglistener;

import org.testng.ITestResult;

/**
 * Created by kcgw001 on 2016/4/22.
 */
public interface TestListener {

    void onTestFailure(ITestResult tr);

    void onTestSkipped(ITestResult tr);

    void onTestSuccess(ITestResult tr);
}
