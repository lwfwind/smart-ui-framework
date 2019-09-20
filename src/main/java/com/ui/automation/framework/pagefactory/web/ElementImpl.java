package com.ui.automation.framework.pagefactory.web;

import com.ui.automation.framework.config.DriverConfig;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.internal.Coordinates;
import org.openqa.selenium.internal.Locatable;
import org.testng.Assert;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * An implementation of the Element interface. Delegates its work to an underlying WebElement instance for
 * custom functionality.
 */
@Slf4j
public class ElementImpl implements Element {
    private final WebElement element;
    private final WebDriver driver;

    /**
     * Creates a Element for a given WebElement.
     *
     * @param driver  the driver
     * @param element element to wrap up
     */
    public ElementImpl(final WebDriver driver, final WebElement element) {
        this.driver = driver;
        this.element = element;
    }

    /**
     * Pause.
     *
     * @param time the time
     */
    public void pause(int time) {
        if (time <= 0) {
            return;
        }
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            log.error(e.getMessage(), e);
        }
    }

    @Override
    public void click() {
        element.click();
    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {
        element.sendKeys(keysToSend);
    }

    @Override
    public Point getLocation() {
        return element.getLocation();
    }

    @Override
    public void submit() {
        element.submit();
    }

    @Override
    public String getAttribute(String name) {
        return element.getAttribute(name);
    }

    @Override
    public String getCssValue(String propertyName) {
        return element.getCssValue(propertyName);
    }

    @Override
    public Dimension getSize() {
        return element.getSize();
    }

    @Override
    public List<WebElement> findElements(By by) {
        return element.findElements(by);
    }

    @Override
    public String getText() {
        return element.getText();
    }

    @Override
    public String getTagName() {
        return element.getTagName();
    }

    @Override
    public boolean isSelected() {
        return element.isSelected();
    }

    @Override
    public WebElement findElement(By by) {
        return element.findElement(by);
    }

    @Override
    public boolean isEnabled() {
        return element.isEnabled();
    }

    @Override
    public boolean isDisplayed() {
        return element.isDisplayed();
    }

    @Override
    public void clear() {
        element.clear();
    }

    @Override
    public WebElement getWrappedElement() {
        return element;
    }

    @Override
    public Coordinates getCoordinates() {
        return ((Locatable) element).getCoordinates();
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> outputType) throws WebDriverException {
        return element.getScreenshotAs(outputType);
    }

    @Override
    public Rectangle getRect() {
        return element.getRect();
    }

    @Override
    public void doubleClick() {
        pause(100);
        (new Actions(driver)).doubleClick(element).perform();
    }

    @Override
    public void mouseOver() {
        // First make mouse out of browser
        Robot rb = null;
        try {
            rb = new Robot();
        } catch (AWTException e) {
            log.error(e.getMessage(), e);
        }
        if (rb != null) {
            rb.mouseMove(0, 0);
        }
        if (DriverConfig.getBrowserType() == DriverConfig.selectedBrowser.GOOGLECHROME) {
            try {
                Actions builder = new Actions(driver);
                builder.moveToElement(element).build().perform();
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }
            return;
        }

        // Firefox and IE require multiple cycles, more than twice, to cause a
        // hovering effect
        if (DriverConfig.getBrowserType() == DriverConfig.selectedBrowser.FIREFOX
                || DriverConfig.getBrowserType() == DriverConfig.selectedBrowser.IE) {
            for (int i = 0; i < 5; i++) {
                Actions builder = new Actions(driver);
                builder.moveToElement(element).build().perform();
            }
            return;
        }

        // Selenium doesn't support the Safari browser
        if (DriverConfig.getBrowserType() == DriverConfig.selectedBrowser.SAFARI) {
            Assert.fail("Mouseover is not supported for Safari now");
        }
        Assert.fail("Incorrect browser type");
    }

    /**
     * Make the WebElement scroll into the view to get focused before
     * click/double-click
     * refer to https://developer.mozilla.org/zh-CN/docs/Web/API/Element/scrollIntoView
     */
    @Override
    public void scrollIntoView(boolean basedByTop) {
        if (basedByTop) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
        } else {
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(false);", element);
        }
    }

    @Override
    public void highLight() {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].style.border='3px solid red'", element);
    }

    @Override
    public String getHideAttribute(String attribute) {
        String scriptGetValue = "return arguments[0].getAttribute('" + attribute + "')";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (String) js.executeScript(scriptGetValue, element);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Object> getAllAttributes() {
        String scriptGetValue = "var items = {}; for (index = 0; index < arguments[0].attributes.length; ++index) { items[arguments[0].attributes[index].name] = arguments[0].attributes[index].value }; return items;";
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return (Map<String, Object>) js.executeScript(scriptGetValue, element);
    }

    @Override
    public void removeReadOnly() {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].removeAttribute('readonly','readonly')", element);
    }

    @Override
    public void addReadOnly() {
        ((JavascriptExecutor) driver).executeScript(
                "arguments[0].setAttribute('readonly','true')", element);
    }
}
