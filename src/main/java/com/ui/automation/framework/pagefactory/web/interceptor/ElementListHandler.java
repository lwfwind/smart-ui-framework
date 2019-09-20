package com.ui.automation.framework.pagefactory.web.interceptor;

import com.ui.automation.framework.cache.DriverCache;
import com.ui.automation.framework.webdriver.Alert;
import com.ui.automation.framework.webdriver.Sleeper;
import com.ui.automation.framework.pagefactory.web.Element;
import com.ui.automation.framework.pagefactory.WithTimeoutProcessor;
import io.appium.java_client.pagefactory.WithTimeout;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.Locatable;
import org.openqa.selenium.internal.WrapsElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.ui.automation.framework.pagefactory.web.ImplementedByProcessor.getImplementClass;

/**
 * Wraps a list of WebElements in multiple wrapped elements.
 */
@Slf4j
public class ElementListHandler implements InvocationHandler {

    /**
     * The Alert.
     */
    protected final Alert alert;
    private final Sleeper sleeper;
    private final WebDriver driver;
    private final ElementLocator locator;
    private final Class<?> interfaceType;
    private final Class<?> implementtingType;
    private final String logicParentElementName;
    private final Field field;

    /**
     * Given an interface and a locator, apply a wrapper over a list of elements.
     *
     * @param interfaceType the interface type
     * @param locator       the locator
     * @param field         the field
     */
    public <T> ElementListHandler(Class<T> interfaceType, ElementLocator locator, Field field) {
        this.driver = DriverCache.get();
        this.locator = locator;
        if (!Element.class.isAssignableFrom(interfaceType)) {
            throw new RuntimeException("interface not assignable to Element.");
        }
        this.interfaceType = interfaceType;
        this.implementtingType = getImplementClass(interfaceType);
        this.logicParentElementName = field.getName();
        this.field = field;
        this.alert = new Alert(driver);
        this.sleeper = new Sleeper();
    }

    /**
     * Executed on invoke of the requested proxy. Used to gather a list of wrapped WebElements.
     *
     * @param o      object to invoke on
     * @param method method to invoke
     * @param paras  parameters for method
     * @return return value from method
     * @throws Throwable when frightened.
     */
    @Override
    public Object invoke(Object o, Method method, Object[] paras) throws Throwable {
        List<Object> wrappedElements = new ArrayList<Object>();
        Constructor<?> cons = implementtingType.getConstructor(WebDriver.class, WebElement.class);
        List<WebElement> elements = null;
        int timeout;
        if (field.isAnnotationPresent(WithTimeout.class)) {
            elements = locator.findElements();
            WithTimeout withTimeout = field.getAnnotation(WithTimeout.class);
            int unit = 1;
            if (withTimeout.unit() == TimeUnit.SECONDS) {
                unit = 1;
            } else if (withTimeout.unit() == TimeUnit.HOURS) {
                unit = 3600;
            } else if (withTimeout.unit() == TimeUnit.MINUTES) {
                unit = 60;
            }
            timeout = (int) withTimeout.time() * unit;
        } else {
            timeout = WithTimeoutProcessor.getTimeOutOfFind(field, 1);
            long end = System.currentTimeMillis() + timeout;
            while (System.currentTimeMillis() < end) {
                elements = locator.findElements();
                if (elements != null && elements.size() > 0) {
                    break;
                }
                this.sleeper.sleep(500);
            }
        }
        if (elements == null) {
            log.error("the " + logicParentElementName
                    + " element can't be found and the time(" + String.valueOf(timeout) + ") is out");
            throw new RuntimeException("the " + logicParentElementName
                    + " element can't be found and the time(" + String.valueOf(timeout) + ") is out");
        }

        for (WebElement element : elements) {
            Object thing = cons.newInstance(driver, element);
            wrappedElements.add(implementtingType.cast(thing));
        }
        try {
            if (method.getName().equals("get")) {
                WebElement element = (WebElement) method.invoke(elements, paras);
                Constructor<?> con = implementtingType.getConstructor(WebDriver.class, WebElement.class);
                Object obj = con.newInstance(this.driver, element);
                InvocationHandler handler = new SubElementHandler(implementtingType.cast(obj), field, (Integer) paras[0]);
                return Proxy.newProxyInstance(
                        this.interfaceType.getClassLoader(), new Class[]{this.interfaceType, WebElement.class, WrapsElement.class, Locatable.class}, handler);
            }
            return method.invoke(wrappedElements, paras);
        } catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            throw e.getCause();
        }
    }


}
