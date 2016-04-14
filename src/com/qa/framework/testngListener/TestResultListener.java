package com.qa.framework.testnglistener;

import com.qa.framework.TestCaseBase;
import com.qa.framework.bean.Method;
import com.qa.framework.cache.DriverCache;
import com.qa.framework.cache.MethodCache;
import com.qa.framework.cache.ResultCache;
import com.qa.framework.config.PropConfig;
import com.qa.framework.library.base.IOHelper;
import com.qa.framework.library.base.StringHelper;
import com.qa.framework.library.webdriver.Action;
import com.qa.framework.library.webdriver.ScreenShot;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.io.File;
import java.util.*;

/**
 * Test result Listener.
 */
public class TestResultListener extends TestListenerAdapter {

    private static Logger logger = Logger.getLogger(TestResultListener.class);

    @Override
    public void onStart(ITestContext testContext) {
        super.onStart(testContext);
        logger.info("testContext Start");
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        onFailure(tr);
        String name = MethodCache.getCurrentMethodName();
        logger.error(name + " Failure");
        setResultCache(tr, "fail");
        TestCaseBase tb = (TestCaseBase) tr.getInstance();
        if (!tb.isUnitTest()) {
            if (!(PropConfig.getCoreType().equalsIgnoreCase("ANDROIDAPP") || PropConfig.getCoreType().equalsIgnoreCase("IOSAPP"))) {
                printAlertInfo(tr);
            }
            saveScreenShot(tr);
            printBrowserInfo();
            printStackTrace(tr);
        }
    }

    /**
     * On failure.
     *
     * @param tr the tr
     */
    public void onFailure(ITestResult tr) {

    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        onSkipped(tr);
        String name = MethodCache.getCurrentMethodName();
        logger.info(name + " Skipped");
        setResultCache(tr, "skip");
        TestCaseBase tb = (TestCaseBase) tr.getInstance();
        if (!tb.isUnitTest()) {
            if (!(PropConfig.getCoreType().equalsIgnoreCase("ANDROIDAPP") || PropConfig.getCoreType().equalsIgnoreCase("IOSAPP"))) {
                printAlertInfo(tr);
            }
            printBrowserInfo();
            printStackTrace(tr);
        }
    }

    /**
     * On skipped.
     *
     * @param tr the tr
     */
    public void onSkipped(ITestResult tr) {

    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        onSuccess(tr);
        String name = MethodCache.getCurrentMethodName();
        logger.info(name + " Success");
        setResultCache(tr, "pass");
        TestCaseBase tb = (TestCaseBase) tr.getInstance();
        if (!tb.isUnitTest()) {
            printBrowserInfo();
        }
        IOHelper.deleteDirectory(ScreenShot.dir + File.separator + "Actions" + File.separator + ScreenShot.time + File.separator + name);
    }

    /**
     * On success.
     *
     * @param tr the tr
     */
    public void onSuccess(ITestResult tr) {

    }

    @Override
    public void onTestStart(ITestResult tr) {
        super.onTestStart(tr);
        String name = MethodCache.getCurrentMethodName();
        logger.info(name + " Start");
        IOHelper.createNestDirectory(ScreenShot.dir + File.separator + "Actions" + File.separator + ScreenShot.time + File.separator + name);
    }

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        logger.info("testContext Finish");

        for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {
            logger.info("PassedTests = " + passedTest.getName());
        }

        for (ITestResult skipTest : testContext.getSkippedTests().getAllResults()) {
            logger.info("SkipTest = " + skipTest.getName());
        }

        for (ITestResult failedTest : testContext.getFailedTests().getAllResults()) {
            logger.info("FailedTest = " + failedTest.getName());
        }

        HashMap<Integer, Method> map = ResultCache.get();
        if (map != null && map.size() > 0) {
            for (Map.Entry<Integer, Method> entry : map.entrySet()) {
                if (entry.getValue().getStatus().equals("fail")) {
                    int currId = entry.getKey();
                    if (map.get(currId + 1) != null && map.get(currId + 1).getStatus().equals("pass") && map.get(currId + 1).getName().equals(map.get(currId).getName())) {
                        for (Iterator<ITestResult> iterator = testContext.getFailedTests().getAllResults().iterator(); iterator.hasNext(); ) {
                            ITestResult testResult = iterator.next();
                            if (entry.getValue().getHashCode() == getId(testResult)) {
                                logger.info("Remove fail but retry pass test: " + entry.getValue().getName());
                                iterator.remove();
                            }
                        }
                    }
                }
            }
        }

    }

    private int getId(ITestResult result) {
        int id = result.getTestClass().getName().hashCode();
        id = id + result.getMethod().getMethodName().hashCode();
        id = id + (result.getParameters() != null ? Arrays.hashCode(result.getParameters()) : 0);
        return id;
    }

    private void saveScreenShot(ITestResult tr) {
        WebDriver driver = DriverCache.get();
        String name = MethodCache.getCurrentMethodName();
        String screenShotPath = ScreenShot.captureFail(driver, name, tr.getName());
        File f = new File(screenShotPath);
        if (f.exists()) {
/*            Reporter.setCurrentTestResult(tr);
            Reporter.logger(screenShotPath);
            //把截图写入到Html报告中方便查看
            Reporter.logger("<img src=\"../" + screenShotPath + "\"/>");*/
        }
    }

    private void printStackTrace(ITestResult tr) {
        StringBuilder stackTrace = new StringBuilder();
        Throwable throwable = tr.getThrowable();
        if (throwable != null) {
            stackTrace.append(throwable.toString());
            stackTrace.append("\n");
            StackTraceElement[] se = throwable.getStackTrace();
            for (StackTraceElement e : se) {
                stackTrace.append("     at ");
                stackTrace.append(e.toString());
                stackTrace.append("\n");
            }
            logger.error(stackTrace);
        }
    }

    private void printBrowserInfo() {
        //captureAction brower error include javascript error
        if (PropConfig.getCoreType().equals("GOOGLECHROME")) {
            WebDriver driver = DriverCache.get();
            LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
            for (LogEntry entry : logEntries) {
                logger.info("brower info - " + entry.getLevel() + " " + entry.getMessage());
            }
        }
    }

    private void setResultCache(ITestResult tr, String result) {
        int id;
        if (ResultCache.get() != null) {
            id = ResultCache.get().size();
        } else {
            id = 0;
        }
        int hashCode = getId(tr);
        Method method = new Method(id + 1, hashCode, tr.getName(), result);
        HashMap<Integer, Method> map = ResultCache.get();
        if (map == null) {
            map = new HashMap<Integer, Method>();
        }
        map.put(id, method);
        ResultCache.set(map);
    }

    private void printAlertInfo(ITestResult tr) {
        TestCaseBase tb = (TestCaseBase) tr.getInstance();
        WebDriver driver = tb.getDriver();
        Action action = new Action(driver);
        List<String> messages = action.acceptAlert(5000);
        if (messages.size() > 0) {
            logger.info(tr.getName() + " alert messages:" + StringHelper.listToString(messages, " \n"));
        }
    }

}
