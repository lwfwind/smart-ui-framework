package com.qa.framework.pagefactory.mobile.interceptor;

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import com.qa.framework.common.Action;
import com.qa.framework.pagefactory.PageFactory;
import com.qa.framework.pagefactory.mobile.AppiumFieldDecorator;
import com.qa.framework.pagefactory.mobile.ThrowableUtil;
import io.appium.java_client.pagefactory.TimeOutDuration;
import io.appium.java_client.pagefactory.Widget;
import io.appium.java_client.pagefactory.bys.ContentType;
import io.appium.java_client.pagefactory.locator.CacheableLocator;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsDriver;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.qa.framework.pagefactory.TimeOutOfFindProcessor.getTimeOutOfFind;
import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.getCurrentContentType;

/**
 * The type Widget interceptor.
 */
public class WidgetInterceptor implements MethodInterceptor {

    /**
     * The Locator.
     */
    protected final ElementLocator locator;
    /**
     * The Driver.
     */
    protected final WebDriver driver;
    /**
     * The Field.
     */
    protected final Field field;
    /**
     * The Action.
     */
    protected final Action action;
    private final Map<ContentType, Constructor<? extends Widget>> instantiationMap;
    private final Map<ContentType, Widget> cachedInstances = new HashMap<>();
    private final TimeOutDuration duration;
    /**
     * The Logger.
     */
    protected Logger logger = Logger.getLogger(WidgetInterceptor.class);
    private WebElement cachedElement;

    /**
     * Instantiates a new Widget interceptor.
     *
     * @param locator          the locator
     * @param driver           the driver
     * @param cachedElement    the cached element
     * @param instantiationMap the instantiation map
     * @param duration         the duration
     * @param field            the field
     */
    public WidgetInterceptor(CacheableLocator locator, WebDriver driver, WebElement cachedElement,
                             Map<ContentType, Constructor<? extends Widget>> instantiationMap,
                             TimeOutDuration duration, Field field) {
        this.cachedElement = cachedElement;
        this.instantiationMap = instantiationMap;
        this.duration = duration;
        this.locator = locator;
        this.driver = driver;
        this.field = field;
        this.action = new Action(driver);
    }

    /**
     * Gets object.
     *
     * @param element the element
     * @param method  the method
     * @param args    the args
     * @return the object
     * @throws Throwable the throwable
     */
    protected Object getObject(WebElement element, Method method, Object[] args) throws Throwable {
        ContentType type = getCurrentContentType(element);
        if (cachedElement == null || (locator != null && !((CacheableLocator) locator).isLookUpCached()) ||
                cachedInstances.size() == 0) {
            cachedElement = element;
            Widget widget = instantiationMap.get(type).newInstance(cachedElement);
            cachedInstances.put(type, widget);
            PageFactory.initElements(new AppiumFieldDecorator(widget, duration), widget);
        }
        try {
            return method.invoke(cachedInstances.get(type), args);
        } catch (Throwable t) {
            throw ThrowableUtil.extractReadableException(t);
        }
    }

    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy proxy) throws Throwable {
        if (locator != null) {
            if (Object.class.equals(method.getDeclaringClass())) {
                return proxy.invokeSuper(obj, args);
            }

            if (WrapsDriver.class.isAssignableFrom(method.getDeclaringClass()) &&
                    method.getName().equals("getWrappedDriver")) {
                return driver;
            }

            WebElement realElement = null;
            List<WebElement> elements = null;
            int timeout = getTimeOutOfFind(field);
            long end = System.currentTimeMillis() + timeout;
            while (System.currentTimeMillis() < end) {
                elements = locator.findElements();
                if (elements != null && elements.size() > 0) {
                    realElement = elements.get(0);
                    if (realElement != null && realElement.isDisplayed()) {
                        break;
                    }
                }
                this.action.pause(500);
            }
            if (realElement == null) {
                logger.error("the " + this.field.getName()
                        + " element can't be found and the time(" + String.valueOf(timeout) + ") is out");
                throw new RuntimeException("the " + this.field.getName()
                        + " element can't be found and the time(" + String.valueOf(timeout) + ") is out");
            }

            return getObject(realElement, method, args);
        }
        return getObject(cachedElement, method, args);
    }
}
