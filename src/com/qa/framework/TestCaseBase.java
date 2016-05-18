package com.qa.framework;

import com.qa.framework.android.DebugBridge;
import com.qa.framework.android.event.AccessibilityEventMonitor;
import com.qa.framework.cache.DriverCache;
import com.qa.framework.cache.MethodCache;
import com.qa.framework.config.DriverConfig;
import com.qa.framework.config.PropConfig;
import com.qa.framework.data.SuiteData;
import com.qa.framework.library.base.StringHelper;
import com.qa.framework.testnglistener.PowerEmailableReporter;
import com.qa.framework.testnglistener.TestResultListener;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ThreadGuard;
import org.testng.ITestContext;
import org.testng.annotations.*;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;

import static com.qa.framework.ioc.AutoInjectHelper.initFields;
import static com.qa.framework.ioc.IocHelper.findImplementClass;

@Listeners({TestResultListener.class, PowerEmailableReporter.class})
public abstract class TestCaseBase {
    protected Logger logger = Logger.getLogger(this.getClass());
    private SuiteData suiteData = null;
    private String browser = null;
    private String hubURL = null;

    @BeforeSuite(alwaysRun = true)
    public void BeforeSuite(ITestContext context) throws Exception {
        logger.info("beforeSuite");
        if (PropConfig.getCoreType().equalsIgnoreCase("ANDROIDAPP")) {
            DebugBridge.init();
            //AccessibilityEventMonitor.start();
        }
        HelperLoader.init();
        Class<?> clazz = findImplementClass(SuiteData.class);
        if (clazz != null) {
            suiteData = (SuiteData) clazz.newInstance();
            suiteData.setup();
        }
        beforeSuite();
    }

    public void beforeSuite() {
    }

    @AfterSuite(alwaysRun = true)
    public void AfterSuite(ITestContext context) throws Exception {
        logger.info("afterSuite");
        Class<?> clazz = findImplementClass(SuiteData.class);
        if (clazz != null) {
            suiteData = (SuiteData) clazz.newInstance();
            suiteData.teardown();
        }
        if (PropConfig.getCoreType().equalsIgnoreCase("ANDROIDAPP")) {
            //AccessibilityEventMonitor.stop();
            DebugBridge.terminate();
        }
        afterSuite();
    }

    public void afterSuite() {
    }

    private void getDriverObj() throws Exception {
        WebDriver driver = null;
        if (!(PropConfig.getCoreType().equalsIgnoreCase("ANDROIDAPP") || PropConfig.getCoreType().equalsIgnoreCase("IOSAPP"))) {
            if (hubURL != null) {
                DesiredCapabilities capability = null;
                if (browser.contains("firefox")) {
                    capability = DesiredCapabilities.firefox();
                } else if (browser.contains("chrome")) {
                    capability = DesiredCapabilities.chrome();
                }
                try {
                    driver = ThreadGuard.protect(new RemoteWebDriver(new URL(hubURL), capability));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            } else {
                driver = DriverConfig.getDriverObject();
            }
        } else {
            driver = DriverConfig.getDriverObject();
        }
        DriverCache.set(driver);
    }

    @Parameters({"browser", "hubURL"})
    @org.testng.annotations.BeforeClass(alwaysRun = true)
    public void BeforeClass(@Optional String browser, @Optional String hubURL) throws Exception {
        logger.info("beforeClass");
        this.browser = browser;
        this.hubURL = hubURL;
        beforeClass();
    }

    public void beforeClass() {
    }

    public boolean isUnitTest() {
        return false;
    }

    @AfterClass(alwaysRun = true)
    public void AfterClass() {
        logger.info("afterClass");
        afterClass();
    }

    public void afterClass() {
    }

    @BeforeMethod(alwaysRun = true)
    public void BeforeMethod(Method method, Object[] para) throws Exception {
        String currentMethodName;
        if (para != null && para.length > 0 && para[0] != null) {
            currentMethodName = method.getName() + "_" + para[0].toString().trim();
        } else {
            currentMethodName = method.getName();
        }
        MethodCache.set(StringHelper.removeSpecialChar(currentMethodName));
        getDriverObj();
        initFields(this);
        beforeMethod(method, para);
        beforeMethod();
    }

    public void beforeMethod(Method method, Object[] para) {
    }

    public void beforeMethod() {
    }

    @AfterMethod(alwaysRun = true)
    public void AfterMethod(Method method, Object[] para) {
        WebDriver driver = DriverCache.get();
        driver.quit();
        afterMethod(method, para);
        afterMethod();
    }

    public void afterMethod(Method method, Object[] para) {
    }

    public void afterMethod() {
    }

}
