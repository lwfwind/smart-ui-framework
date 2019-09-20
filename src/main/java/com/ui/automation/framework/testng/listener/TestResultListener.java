package com.ui.automation.framework.testng.listener;

import com.library.common.IOHelper;
import com.library.common.StringHelper;
import com.ui.automation.framework.TestCaseBase;
import com.ui.automation.framework.testng.listener.bean.Method;
import com.ui.automation.framework.cache.DriverCache;
import com.ui.automation.framework.cache.MethodCache;
import com.ui.automation.framework.cache.ResultCache;
import com.ui.automation.framework.webdriver.Alert;
import com.ui.automation.framework.webdriver.ScreenShot;
import com.ui.automation.framework.config.PropConfig;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import java.io.File;
import java.util.*;

/**
 * Test result Listener.
 */
@Slf4j
public class TestResultListener extends TestListenerAdapter {

    @Override
    public void onStart(ITestContext testContext) {
        super.onStart(testContext);
        log.info("testContext Start");
        Object obj = ListenerHelper.findImplementClass(ICustomTestListener.class);
        if (obj != null) {
            ICustomTestListener testListenerImp = null;
            testListenerImp = (ICustomTestListener) obj;
            testListenerImp.onStart(testContext);
        }
    }

    @Override
    public void onTestFailure(ITestResult tr) {
        super.onTestFailure(tr);
        String name = MethodCache.getCurrentMethodName();
        log.error(name + " Failure");
        setResultCache(tr, "fail");
        boolean isUnitTest = false;
        if (tr.getInstance() instanceof TestCaseBase) {
            TestCaseBase tb = (TestCaseBase) tr.getInstance();
            isUnitTest = tb.isUnitTest();
        } else {
            TestCaseBase tb = (TestCaseBase) tr.getInstance();
            isUnitTest = tb.isUnitTest();
        }
        if (!isUnitTest) {
            if (!(PropConfig.get().getCoreType().equalsIgnoreCase("ANDROIDAPP") || PropConfig.get().getCoreType().equalsIgnoreCase("IOSAPP"))) {
                printAlertInfo(tr);
            }
            saveScreenShot(tr);
            printBrowserInfo();
            printStackTrace(tr);
        }
        Object obj = ListenerHelper.findImplementClass(ICustomTestListener.class);
        if (obj != null) {
            ICustomTestListener testListenerImp = null;
            testListenerImp = (ICustomTestListener) obj;
            testListenerImp.onTestFailure(tr);
        }
    }

    @Override
    public void onTestSkipped(ITestResult tr) {
        super.onTestSkipped(tr);
        String name = MethodCache.getCurrentMethodName();
        log.info(name + " Skipped");
        setResultCache(tr, "skip");
        TestCaseBase tb = (TestCaseBase) tr.getInstance();
        if (!tb.isUnitTest()) {
            printBrowserInfo();
            printStackTrace(tr);
        }
        Object obj = ListenerHelper.findImplementClass(ICustomTestListener.class);
        if (obj != null) {
            ICustomTestListener testListenerImp = null;
            testListenerImp = (ICustomTestListener) obj;
            testListenerImp.onTestSkipped(tr);
        }
    }

    @Override
    public void onTestSuccess(ITestResult tr) {
        super.onTestSuccess(tr);
        String name = MethodCache.getCurrentMethodName();
        log.info(name + " Success");
        setResultCache(tr, "pass");
        boolean isUnitTest = false;
        if (tr.getInstance() instanceof TestCaseBase) {
            TestCaseBase tb = (TestCaseBase) tr.getInstance();
            isUnitTest = tb.isUnitTest();
        } else {
            TestCaseBase tb = (TestCaseBase) tr.getInstance();
            isUnitTest = tb.isUnitTest();
        }
        if (!isUnitTest) {
            printBrowserInfo();
        }
        IOHelper.deleteDirectory(ScreenShot.dir + File.separator + "Actions" + File.separator + ScreenShot.time + File.separator + name);
        Object obj = ListenerHelper.findImplementClass(ICustomTestListener.class);
        if (obj != null) {
            ICustomTestListener testListenerImp = null;
            testListenerImp = (ICustomTestListener) obj;
            testListenerImp.onTestSuccess(tr);
        }
    }

    @Override
    public void onTestStart(ITestResult tr) {
        super.onTestStart(tr);
        String name = MethodCache.getCurrentMethodName();
        log.info(name + " Start");
        IOHelper.createNestDirectory(ScreenShot.dir + File.separator + "Actions" + File.separator + ScreenShot.time + File.separator + name);
        Object obj = ListenerHelper.findImplementClass(ICustomTestListener.class);
        if (obj != null) {
            ICustomTestListener testListenerImp = null;
            testListenerImp = (ICustomTestListener) obj;
            testListenerImp.onTestStart(tr);
        }
    }

    @Override
    public void onFinish(ITestContext testContext) {
        super.onFinish(testContext);
        log.info("testContext Finish");
        Object obj = ListenerHelper.findImplementClass(ICustomTestListener.class);
        if (obj != null) {
            ICustomTestListener testListenerImp = null;
            testListenerImp = (ICustomTestListener) obj;
            testListenerImp.onFinish(testContext);
        }
        for (ITestResult passedTest : testContext.getPassedTests().getAllResults()) {
            log.info("PassedTests = " + passedTest.getName());
        }

        for (ITestResult skipTest : testContext.getSkippedTests().getAllResults()) {
            log.info("SkipTest = " + skipTest.getName());
        }

        for (ITestResult failedTest : testContext.getFailedTests().getAllResults()) {
            log.info("FailedTest = " + failedTest.getName());
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
                                log.info("Remove fail but retry pass test: " + entry.getValue().getName());
                                iterator.remove();
                                break;
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
        if (System.getProperty("screenshotBaseURL") != null) {
            Reporter.setCurrentTestResult(tr);
            //把截图写入到Html报告中方便查看
            Reporter.log("<img src=\"" + screenShotPath + "\"/>");
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
            log.error(String.valueOf(stackTrace));
        }
    }

    private void printBrowserInfo() {
        //captureAction brower error include javascript error
        if (PropConfig.get().getCoreType().equals("GOOGLECHROME")) {
            WebDriver driver = DriverCache.get();
            try {
                LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
                for (LogEntry entry : logEntries) {
                    log.debug("brower info - " + entry.getLevel() + " " + entry.getMessage());
                }
            } catch (Exception ignored) {
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
        map.put(id + 1, method);
        ResultCache.set(map);
    }

    private void printAlertInfo(ITestResult tr) {
        WebDriver driver = DriverCache.get();
        Alert alert = new Alert(driver);
        List<String> messages = alert.acceptAlert(5000);
        if (messages.size() > 0) {
            log.info(tr.getName() + " alert messages:" + StringHelper.join(messages, " \n"));
        }
    }

}
