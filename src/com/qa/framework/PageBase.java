package com.qa.framework;

import com.qa.framework.cache.DriverCache;
import com.qa.framework.library.android.uiautomator.UiAutomatorHelper;
import com.qa.framework.library.webdriver.Action;
import com.qa.framework.pagefactory.PageFactory;
import com.qa.framework.pagefactory.web.Element;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;

import java.io.IOException;
import java.net.URL;
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
    protected final Logger logger = Logger.getLogger(this.getClass());
    /**
     * The Random.
     */
    protected final Random random = new Random();
    /**
     * The Driver.
     */
    public WebDriver driver;
    /**
     * The Action.
     */
    public Action action;

    /**
     * Instantiates a new Page base.
     */
    public PageBase() {
        this.driver = DriverCache.get();
        if (this.driver != null) {
            action = new Action(driver);
            if (!isMobilePlat()) {
                driver.manage().timeouts().pageLoadTimeout(120000, TimeUnit.MILLISECONDS);
            }
            initElements(this);
        }
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
            driver.get(resource);
        } else {
            URL formsHtmlUrl = PageBase.class.getClassLoader().getResource(resource);
            if (formsHtmlUrl == null) {
                throw new RuntimeException();
            }
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
     * Maximize.
     */
    public void maximize() {
        driver.manage().window().maximize();
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
            action.acceptAlert();
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
        action.pause(1000);
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
        action.selectLastOpenedWindow();
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
     * Hide keyboard.
     */
    public void hideKeyboard() {
        AndroidDriver androidDriver = (AndroidDriver) driver;
        try {
            androidDriver.hideKeyboard();
        } catch (WebDriverException ex) {
            logger.info("<Keyboard>Soft keyboard not present, cannot hide keyboard!!!");
        }
    }

    /**
     * Swipe to up.
     *
     * @param driver the driver
     * @param during the during
     */
    public void swipeToUp(WebDriver driver, int during) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        ((AppiumDriver) driver).swipe(width / 2, height * 3 / 4, width / 2, height / 4, during);
        pause(3000);
    }

    /**
     * Swipe to down.
     *
     * @param driver the driver
     * @param during the during
     */
    public void swipeToDown(WebDriver driver, int during) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        ((AppiumDriver) driver).swipe(width / 2, height / 4, width / 2, height * 3 / 4, during);
        pause(3000);
    }

    /**
     * Swipe to left.
     *
     * @param driver the driver
     * @param during the during
     */
    public void swipeToLeft(WebDriver driver, int during) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        ((AppiumDriver) driver).swipe(width * 7 / 8, height / 2, width / 8, height / 2, during);
        pause(3000);
    }

    /**
     * Swipe to right.
     *
     * @param driver the driver
     * @param during the during
     */
    public void swipeToRight(WebDriver driver, int during) {
        int width = driver.manage().window().getSize().width;
        int height = driver.manage().window().getSize().height;
        ((AppiumDriver) driver).swipe(width / 8, height / 2, width * 7 / 8, height / 2, during);
        pause(3000);
    }

    /**
     * Tap eelment by coord.
     *
     * @param element   the element
     * @param distanceX the distance x
     * @param distanceY the distance y
     */
    public void tapEelmentByCoord(WebElement element, int distanceX, int distanceY) {
        int startX = element.getLocation().getX();
        int startY = element.getLocation().getY();
        int endX = element.getSize().getWidth() + startX;
        int endY = element.getSize().getHeight() + startY;
        int aimY = endY + distanceY;
        int aimX = endX + distanceX;
        AndroidDriver androidDriver = (AndroidDriver) driver;
        androidDriver.tap(1, aimX, aimY, 100);
        logger.info("点击的坐标为:(" + aimX + "," + aimY + ")");
    }

    /**
     * Refresh ui hierarchy for android PopupWindow.
     */
    public void refreshUiHierarchy(){
        try {
            UiAutomatorHelper.searchUiHierarchyContent("");
        } catch (UiAutomatorHelper.UiAutomatorException | IOException e) {
            e.printStackTrace();
        }
    }


}
