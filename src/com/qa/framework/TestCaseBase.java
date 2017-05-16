package com.qa.framework;

import com.library.common.StringHelper;
import com.qa.framework.android.DebugBridge;
import com.qa.framework.cache.DriverCache;
import com.qa.framework.cache.MethodCache;
import com.qa.framework.config.DriverConfig;
import com.qa.framework.config.PropConfig;
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

/**
 * The type Test case base.
 */
@Listeners({TestResultListener.class, PowerEmailableReporter.class})
public abstract class TestCaseBase {
    /**
     * The constant logger.
     */
    protected static Logger logger = Logger.getLogger(TestCaseBase.class);
    private String browser = null;
    private String hubURL = null;

    /**
     * Before suite.
     *
     * @param context the context
     * @throws Exception the exception
     */
    @BeforeSuite(alwaysRun = true)
    public void BeforeSuite(ITestContext context) throws Exception {
        logger.info("beforeSuite");
        beforeSuite();
        if (PropConfig.getCoreType().equalsIgnoreCase("ANDROIDAPP")) {
            DebugBridge.init();
            //AccessibilityEventMonitor.start();
        }
        HelperLoader.init();
    }

    /**
     * Before suite.
     */
    public void beforeSuite() {
    }

    /**
     * After suite.
     *
     * @param context the context
     * @throws Exception the exception
     */
    @AfterSuite(alwaysRun = true)
    public void AfterSuite(ITestContext context) throws Exception {
        logger.info("afterSuite");
        afterSuite();
        if (PropConfig.getCoreType().equalsIgnoreCase("ANDROIDAPP")) {
            //AccessibilityEventMonitor.stop();
            DebugBridge.terminate();
        }
    }

    /**
     * After suite.
     */
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
                    logger.error(e.getMessage(), e);
                }
            } else {
                driver = DriverConfig.getDriverObject();
            }
        } else {
            driver = DriverConfig.getDriverObject();
        }
        DriverCache.set(driver);
    }

    /**
     * Before class.
     *
     * @param browser the browser
     * @param hubURL  the hub url
     * @throws Exception the exception
     */
    @Parameters({"browser", "hubURL"})
    @org.testng.annotations.BeforeClass(alwaysRun = true)
    public void BeforeClass(@Optional String browser, @Optional String hubURL) throws Exception {
        logger.info("beforeClass");
        this.browser = browser;
        this.hubURL = hubURL;
        beforeClass();
    }

    /**
     * Before class.
     */
    public void beforeClass() {
    }

    /**
     * Is unit test boolean.
     *
     * @return the boolean
     */
    public boolean isUnitTest() {
        return false;
    }

    /**
     * After class.
     */
    @AfterClass(alwaysRun = true)
    public void AfterClass() {
        logger.info("afterClass");
        afterClass();
    }

    /**
     * After class.
     */
    public void afterClass() {
    }

    /**
     * Before method.
     *
     * @param method the method
     * @param para   the para
     * @throws Exception the exception
     */
    @BeforeMethod(alwaysRun = true)
    public void BeforeMethod(Method method, Object[] para) throws Exception {
        beforeMethod(method, para);
        beforeMethod();
        String currentMethodName;
        if (para != null && para.length > 0 && para[0] != null) {
            currentMethodName = method.getName() + "_" + para[0].toString().trim();
        } else {
            currentMethodName = method.getName();
        }
        MethodCache.set(StringHelper.removeSpecialChar(currentMethodName));
        getDriverObj();
        initFields(this);
    }

    /**
     * Before method.
     *
     * @param method the method
     * @param para   the para
     */
    public void beforeMethod(Method method, Object[] para) {
    }

    /**
     * Before method.
     */
    public void beforeMethod() {
    }

    /**
     * After method.
     *
     * @param method the method
     * @param para   the para
     */
    @AfterMethod(alwaysRun = true)
    public void AfterMethod(Method method, Object[] para) {
        afterMethod(method, para);
        afterMethod();
        WebDriver driver = DriverCache.get();
        driver.quit();
    }

    /**
     * After method.
     *
     * @param method the method
     * @param para   the para
     */
    public void afterMethod(Method method, Object[] para) {
    }

    /**
     * After method.
     */
    public void afterMethod() {
    }

}
