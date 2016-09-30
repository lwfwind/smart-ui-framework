package com.qa.framework;

import com.qa.framework.android.uiautomator.UiAutomatorHelper;
import com.qa.framework.cache.DriverCache;
import com.qa.framework.common.Alert;
import com.qa.framework.common.Sleeper;
import com.qa.framework.config.PropConfig;
import com.qa.framework.pagefactory.PageFactory;
import com.qa.framework.pagefactory.web.Element;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

import java.net.URL;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.getPlatform;

/**
 * The type Page base.
 */
public abstract class PageBase {

    /**
     * The Logger.
     */
    protected static Logger logger = Logger.getLogger(PageBase.class);
    /**
     * The Random.
     */
    protected final Random random = new Random();
    /**
     * The Driver.
     */
    public WebDriver driver;

    /**
     * The Android driver.
     */
    public AndroidDriver androidDriver;
    /**
     * The Ios driver.
     */
    public IOSDriver iosDriver;
    /**
     * The Alert.
     */
    public Alert alert;
    public Sleeper sleeper;

    /**
     * Instantiates a new Page base.
     */
    public PageBase() {
        this.driver = DriverCache.get();
        if (this.driver != null) {
            alert = new Alert(driver);
            sleeper = new Sleeper();
            if (!isMobilePlat()) {
                driver.manage().timeouts().pageLoadTimeout(120000, TimeUnit.MILLISECONDS);
            }
            initElements(this);
        }
    }

    /**
     * Gets android driver.
     *
     * @return the android driver
     */
    public AndroidDriver getAndroidDriver() {
        if (getPlatform(driver).equals(MobilePlatform.ANDROID)) {
            androidDriver = (AndroidDriver) driver;
        }
        return androidDriver;
    }

    /**
     * Gets ios driver.
     *
     * @return the ios driver
     */
    public IOSDriver getIOSDriver() {
        if (getPlatform(driver).equals(MobilePlatform.IOS)) {
            iosDriver = (IOSDriver) driver;
        }
        return iosDriver;
    }

    /**
     * Gets appium driver.
     *
     * @return the appium driver
     */
    public AppiumDriver getAppiumDriver() {
        return (AppiumDriver) driver;
    }

    /**
     * Is mobile plat boolean.
     *
     * @return the boolean
     */
    public boolean isMobilePlat() {
        return (getPlatform(driver).equals(MobilePlatform.ANDROID) || getPlatform(driver).equals(MobilePlatform.IOS) || getPlatform(driver).equals(MobilePlatform.FIREFOX_OS));
    }

    /**
     * Open url or a page by its name. page is stored in test resources.
     *
     * @param resource String containing the name of your test html.
     */
    public void open(String resource) {
        if (resource.startsWith("http")) {
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            driver.get(resource);
        } else {
            URL formsHtmlUrl = PageBase.class.getClassLoader().getResource(resource);
            if (formsHtmlUrl == null) {
                throw new RuntimeException();
            }
            driver.manage().window().maximize();
            driver.manage().deleteAllCookies();
            this.driver.get(formsHtmlUrl.toString());
        }
    }

    /**
     * Init elements.
     *
     * @param page the page
     */
    public void initElements(Object page) {
        PageFactory.initElements(driver, page);
    }


    /**
     * Pause
     *
     * @param time in millisecond
     */
    public void pause(int time) {
        if (time <= 0) {
            return;
        }
        try {
            Thread.sleep(time);
            //logger.info("Pause " + time + " ms");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accept alert.
     */
    public void acceptAlert() {
        try {
            alert.acceptAlert();
        } catch (NoAlertPresentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Accept alert and refresh.
     */
    public void acceptAlertAndRefresh() {
        long currentTime = System.currentTimeMillis();
        long maxTime = currentTime + 3000;
        while (currentTime < maxTime) {
            acceptAlert();
            currentTime = System.currentTimeMillis();
        }
        sleeper.sleep();
        initElements(this);
    }

    /**
     * Gets page title.
     *
     * @return the page title
     */
    public String getPageTitle() {
        return driver.getTitle();
    }

    /**
     * Enter i frame.
     *
     * @param webElement the web element
     */
    public void enterIFrame(Element webElement) {
        driver.switchTo().frame(webElement);
    }

    /**
     * Leave i frame.
     */
    public void leaveIFrame() {
        driver.switchTo().defaultContent();
    }

    /**
     * Close tab.
     */
    public void closeTab() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("close()");
        alert.selectLastOpenedWindow();
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl() {
        return driver.getCurrentUrl();
    }

    /**
     * Gets page source.
     *
     * @return the page source
     */
    public String getPageSource() {
        if (getPlatform(driver).equals(MobilePlatform.ANDROID)) {
            return UiAutomatorHelper.getUiHierarchyContent();
        } else if (getPlatform(driver).equals(MobilePlatform.IOS)) {
            //
        }
        return driver.getPageSource();
    }

    /**
     * Hide keyboard.
     */
    public void hideKeyboard() {
        if (getPlatform(driver).equals(MobilePlatform.ANDROID)) {
            AndroidDriver androidDriver = (AndroidDriver) driver;
            try {
                androidDriver.hideKeyboard();
            } catch (WebDriverException ex) {
                logger.info("<Keyboard>Soft keyboard not present, cannot hide keyboard!!!");
            }
        }
    }

    /**
     * Swipe to up.
     */
    public void swipeToUp() {
        pause(3000);
        switch (PropConfig.getCoreType()) {
            case "ANDROIDAPP":
                int width = driver.manage().window().getSize().width;
                int height = driver.manage().window().getSize().height;
                ((AppiumDriver) driver).swipe(width / 2, height * 3 / 4, width / 2, height / 4, 1000);
                break;
            case "IOSAPP":
                JavascriptExecutor js = (JavascriptExecutor) driver;
                HashMap<String, String> scrollObject = new HashMap<String, String>();
                scrollObject.put("direction", "down");
                js.executeScript("mobile: scroll", scrollObject);
                break;
        }
        pause(1000);
    }

    /**
     * Swipe to down.
     */
    public void swipeToDown() {
        pause(3000);
        switch (PropConfig.getCoreType()) {
            case "ANDROIDAPP":
                int width = driver.manage().window().getSize().width;
                int height = driver.manage().window().getSize().height;
                ((AppiumDriver) driver).swipe(width / 2, height / 4, width / 2, height * 3 / 4, 1000);
                break;
            case "IOSAPP":
                JavascriptExecutor js = (JavascriptExecutor) driver;
                HashMap<String, String> scrollObject = new HashMap<String, String>();
                scrollObject.put("direction", "up");
                js.executeScript("mobile: scroll", scrollObject);
                break;
        }
        pause(1000);
    }

    /**
     * Swipe to left.
     */
    public void swipeToLeft() {
        pause(3000);
        switch (PropConfig.getCoreType()) {
            case "ANDROIDAPP":
                int width = driver.manage().window().getSize().width;
                int height = driver.manage().window().getSize().height;
                ((AppiumDriver) driver).swipe(width * 7 / 8, height / 2, width / 8, height / 2, 1000);
                break;
            case "IOSAPP":
                JavascriptExecutor js = (JavascriptExecutor) driver;
                HashMap<String, String> scrollObject = new HashMap<String, String>();
                scrollObject.put("direction", "right");
                js.executeScript("mobile: scroll", scrollObject);
                break;
        }
        pause(1000);
    }

    /**
     * Swipe to right.
     */
    public void swipeToRight() {
        pause(3000);
        switch (PropConfig.getCoreType()) {
            case "ANDROIDAPP":
                int width = driver.manage().window().getSize().width;
                int height = driver.manage().window().getSize().height;
                ((AppiumDriver) driver).swipe(width / 8, height / 2, width * 7 / 8, height / 2, 1000);
                break;
            case "IOSAPP":
                JavascriptExecutor js = (JavascriptExecutor) driver;
                HashMap<String, String> scrollObject = new HashMap<String, String>();
                scrollObject.put("direction", "left");
                js.executeScript("mobile: scroll", scrollObject);
                break;
        }
        pause(1000);
    }


}
