package com.qa.framework.pagefactory.web.interceptor;

import com.qa.framework.cache.DriverCache;
import com.qa.framework.cache.ElementCache;
import com.qa.framework.cache.MethodCache;
import com.qa.framework.common.Action;
import com.qa.framework.common.ScreenShot;
import com.qa.framework.pagefactory.web.Element;
import io.appium.java_client.pagefactory.WithTimeout;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.List;

import static com.qa.framework.pagefactory.WithTimeoutProcessor.getTimeOutOfFind;
import static com.qa.framework.pagefactory.web.ImplementedByProcessor.getImplementClass;
import static com.qa.framework.pagefactory.web.ScrollIntoViewProcessor.getScrollIntoView;

/**
 * Replaces DefaultLocatingElementHandler. Simply opens it up to descendants of the WebElement interface, and other
 * mix-ins of WebElement and Locatable, etc. Saves the wrapping type for calling the constructor of the wrapped classes.
 */
public class ElementHandler implements InvocationHandler {
    /**
     * The Action.
     */
    protected final Action action;
    private final WebDriver driver;
    private final ElementLocator locator;
    private final Class<?> implementtingType;
    private final String logicElementName;
    private final Field field;
    /**
     * The Logger.
     */
    protected Logger logger = Logger.getLogger(ElementHandler.class);

    /**
     * Generates a handler to retrieve the WebElement from a locator for a given WebElement interface descendant.
     *
     * @param interfaceType the interface type
     * @param locator       the locator
     * @param field         the field
     */
    public <T> ElementHandler(Class<T> interfaceType, ElementLocator locator, Field field) {
        this.driver = DriverCache.get();
        this.locator = locator;
        if (!Element.class.isAssignableFrom(interfaceType)) {
            throw new RuntimeException("interface not assignable to Element.");
        }
        this.implementtingType = getImplementClass(interfaceType);
        this.logicElementName = field.getName();
        this.field = field;
        this.action = new Action(driver);
    }

    @Override
    public Object invoke(Object object, Method method, Object[] paras) throws Throwable {
        Element wapperElement = null;
        List<WebElement> elements = null;
        WebElement element = null;
        int previousWindowsCount = driver.getWindowHandles().size();
        long timeout = 0;
        if (field.isAnnotationPresent(WithTimeout.class)) {
            WithTimeout withTimeout = field.getAnnotation(WithTimeout.class);
            timeout = withTimeout.unit().toMillis(withTimeout.time());
        } else {
            timeout = getTimeOutOfFind(field, 1);
        }
        while (System.currentTimeMillis() < System.currentTimeMillis() + timeout) {
            elements = locator.findElements();
            if (elements.size() > 1) {
                boolean isFound = false;
                for(WebElement e:elements){
                    if (e.isDisplayed()) {
                        element = e;
                        isFound = true;
                        break;
                    }
                }
                if(isFound){
                    break;
                }
            }
            else
            {
                element = elements.get(0);
                break;
            }
            this.action.pause(500);
        }
        if (element == null) {
            logger.error("the " + logicElementName
                    + " element can't be found and the time(" + String.valueOf(timeout) + ") is out");
            throw new RuntimeException("the " + logicElementName
                    + " element can't be found and the time(" + String.valueOf(timeout) + ") is out");
        }

        if ("getWrappedElement".equals(method.getName())) {
            return element;
        }

        Constructor<?> cons = implementtingType.getConstructor(WebDriver.class, WebElement.class);
        Object obj = cons.newInstance(driver, element);

        try {
            wapperElement = (Element) obj;
            if (method.getName().equals("click") || method.getName().equals("sendKeys") || method.getName().equals("mouseOver")) {
                wapperElement.scrollIntoView(getScrollIntoView(field));
                this.action.pause(50);
                wapperElement.highLight();
                this.action.pause(50);
            }
            Object ret = null;

            if (method.getName().equals("click")) {
                String currMethodName = MethodCache.getCurrentMethodName();
                ScreenShot.captureAction(driver, currMethodName, logicElementName);
                ret = method.invoke(obj, paras);
                ElementCache.set(element);
                logger.info(logicElementName + " click");
                this.action.pause(500);
                if (driver.getWindowHandles().size() > previousWindowsCount) {
                    this.action.selectLastOpenedWindow();
                }
            } else {
                if (paras != null) {
                    logger.info(logicElementName + " " + method.getName() + " " + Arrays.deepToString(paras));
                } else {
                    logger.info(logicElementName + " " + method.getName());
                }
                ret = method.invoke(obj, paras);
                this.action.pause(500);
            }
            return ret;
        } catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            if (e.getCause().toString().contains("not clickable") || e.getCause().toString().contains("stale element reference")) {
                logger.info(e.getCause().toString());
                this.action.pause(5000);
                wapperElement.click();
                this.action.pause(2000);
                return "action.click";
            } else {
                throw e.getCause();
            }
        }
    }
}
