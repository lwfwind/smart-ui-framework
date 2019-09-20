package com.ui.automation.framework.webdriver;

import com.ui.automation.framework.android.uiautomator.UiAutomatorHelper;
import io.appium.java_client.remote.MobilePlatform;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.Set;

import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.getPlatform;

/**
 * The type Alert.
 */
@Slf4j
public class Window {

    /**
     * The Driver.
     */
    public WebDriver driver;
    private Sleeper sleeper = new Sleeper();

    /**
     * Instantiates a new Alert.
     *
     * @param driver the driver
     */
    public Window(WebDriver driver) {
        this.driver = driver;
    }

    /**
     * Select last opened window.
     */
    public void selectLastOpenedWindow() {
        Set<String> windowHandles = driver.getWindowHandles();
        int index = windowHandles.size() - 1;
        int count = 0;
        for (String handler : windowHandles) {
            if (count != index) {
                count++;
            } else {
                driver.switchTo().window(handler);
            }
        }
    }

    /**
     * Close current window.
     */
    public void closeCurrentWindow() {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("close()");
        selectLastOpenedWindow();
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

}
