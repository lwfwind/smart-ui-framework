package com.ui.automation.framework.common;

import com.ui.automation.framework.config.PropConfig;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.HashMap;

public class Swipe {
    private Sleeper sleeper = new Sleeper();
    private WebDriver driver;

    public Swipe(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Swipe to up.
     */
    public void swipeToUp() {
        sleeper.sleep(3000);
        switch (PropConfig.get().getCoreType()) {
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
        sleeper.sleep(1000);
    }

    /**
     * Swipe to down.
     */
    public void swipeToDown() {
        sleeper.sleep(3000);
        switch (PropConfig.get().getCoreType()) {
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
        sleeper.sleep(1000);
    }

    /**
     * Swipe to left.
     */
    public void swipeToLeft() {
        sleeper.sleep(3000);
        switch (PropConfig.get().getCoreType()) {
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
        sleeper.sleep(1000);
    }

    /**
     * Swipe to right.
     */
    public void swipeToRight() {
        sleeper.sleep(3000);
        switch (PropConfig.get().getCoreType()) {
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
        sleeper.sleep(1000);
    }
}
