package com.ui.automation.framework.webdriver;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.remote.MobilePlatform;
import org.openqa.selenium.WebDriver;

import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.getPlatform;

public class Driver {
    private WebDriver driver;

    public Driver(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Gets android driver.
     *
     * @return the android driver
     */
    public AndroidDriver getAndroidDriver() {
        if (getPlatform(driver).equals(MobilePlatform.ANDROID)) {
            return (AndroidDriver) driver;
        }
        return null;
    }

    /**
     * Gets ios driver.
     *
     * @return the ios driver
     */
    public IOSDriver getIOSDriver() {
        if (getPlatform(driver).equals(MobilePlatform.IOS)) {
            return (IOSDriver) driver;
        }
        return null;
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
}
