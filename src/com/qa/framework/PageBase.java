package com.qa.framework;

import com.qa.framework.cache.DriverCache;
import com.qa.framework.library.webdriver.Action;
import com.qa.framework.pagefactory.PageFactory;
import com.qa.framework.pagefactory.web.Element;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import java.util.Random;
import java.util.concurrent.TimeUnit;

import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.getPlatform;

public abstract class PageBase {

    protected final Logger logger = Logger.getLogger(this.getClass());
    protected final Random random = new Random();
    public WebDriver driver;
    public Action action;

    public PageBase() {
        this.driver = DriverCache.get();
        if (this.driver != null) {
            if (!isMobilePlat()) {
                action = new Action(driver);
                driver.manage().timeouts().pageLoadTimeout(120000, TimeUnit.MILLISECONDS);
            }
            initElements(this);
        }
    }

    public boolean isMobilePlat() {
        return (getPlatform(driver).equals(MobilePlatform.ANDROID) || getPlatform(driver).equals(MobilePlatform.IOS) || getPlatform(driver).equals(MobilePlatform.FIREFOX_OS));
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

    public void acceptAlert() {
        try {
            action.acceptAlert();
        } catch (NoAlertPresentException e) {
        }
    }

    public void acceptAlertAndRefresh() {
        long currentTime = System.currentTimeMillis();
        long maxTime = currentTime + 3000;
        while (currentTime < maxTime) {
            acceptAlert();
            currentTime = System.currentTimeMillis();
        }
        action.pause(1000);
        initElements(this);
    }

    public void initElements(Object page) {
        PageFactory.initElements(driver, page);
    }

    public void maximize() {
        driver.manage().window().maximize();
    }

    public void open(String url) {
        try {
            driver.get(url);
        } catch (TimeoutException e) {
        }
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    public void enterIFrame(Element webElement) {
        driver.switchTo().frame(webElement);
    }

    public void leaveIFrame() {
        driver.switchTo().defaultContent();
    }

    public void closeTab() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("close()");
        action.selectLastOpenedWindow();
    }

    public String getUrl() {
        return driver.getCurrentUrl();
    }

    public void swipeToUp(WebDriver driver, int during) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        ((AppiumDriver) driver).swipe(width / 2, height * 7 / 8, width / 2, height / 8, during);
        pause(3000);
    }

    public void swipeToDown(WebDriver driver, int during) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        ((AppiumDriver) driver).swipe(width / 2, height / 8, width / 2, height * 7 / 8, during);
        pause(3000);
    }

    public void swipeToLeft(WebDriver driver, int during) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        ((AppiumDriver) driver).swipe(width * 7 / 8, height / 2, width / 8, height / 2, during);
        pause(3000);
    }

    public void swipeToRight(WebDriver driver, int during) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        ((AppiumDriver) driver).swipe(width / 8, height / 2, width * 7 / 8, height / 2, during);
        pause(3000);
    }
}
