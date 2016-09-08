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

import com.qa.framework.common.Alert;
import com.qa.framework.common.Sleeper;
import com.qa.framework.pagefactory.mobile.ThrowableUtil;
import io.appium.java_client.pagefactory.TimeOutDuration;
import io.appium.java_client.pagefactory.Widget;
import io.appium.java_client.pagefactory.WithTimeout;
import io.appium.java_client.pagefactory.bys.ContentType;
import io.appium.java_client.pagefactory.locator.CacheableLocator;
import io.appium.java_client.pagefactory.utils.ProxyFactory;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.pagefactory.ElementLocator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.qa.framework.pagefactory.WithTimeoutProcessor.getTimeOutOfFind;
import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.getCurrentContentType;


/**
 * The type Widget list interceptor.
 */
public class WidgetListInterceptor implements MethodInterceptor {

    /**
     * The Locator.
     */
    protected final ElementLocator locator;
    /**
     * The Field.
     */
    protected final Field field;
    private final Map<ContentType, Constructor<? extends Widget>> instantiationMap;
    private final List<Widget> cachedWidgets = new ArrayList<>();
    private final Class<? extends Widget> declaredType;
    private final TimeOutDuration duration;
    private final WebDriver driver;
    private final Sleeper sleeper;
    /**
     * The Logger.
     */
    protected Logger logger = Logger.getLogger(WidgetListInterceptor.class);
    private List<WebElement> cachedElements;

    /**
     * Instantiates a new Widget list interceptor.
     *
     * @param locator          the locator
     * @param driver           the driver
     * @param instantiationMap the instantiation map
     * @param declaredType     the declared type
     * @param duration         the duration
     * @param field            the field
     */
    public WidgetListInterceptor(CacheableLocator locator, WebDriver driver, Map<ContentType, Constructor<? extends Widget>> instantiationMap,
                                 Class<? extends Widget> declaredType, TimeOutDuration duration, Field field) {
        this.locator = locator;
        this.instantiationMap = instantiationMap;
        this.declaredType = declaredType;
        this.duration = duration;
        this.driver = driver;
        this.field = field;
        this.sleeper = new Sleeper();
    }

    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy proxy) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return proxy.invokeSuper(obj, args);
        }

        List<WebElement> realElements = null;
        int timeout;
        if (field.isAnnotationPresent(WithTimeout.class)) {
            realElements = locator.findElements();
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
            timeout = getTimeOutOfFind(field, 2);
            long end = System.currentTimeMillis() + timeout;
            while (System.currentTimeMillis() < end) {
                realElements = locator.findElements();
                if (realElements != null && realElements.size() > 0) {
                    break;
                }
                this.sleeper.sleep(500);
            }

        }
        if (realElements == null) {
            logger.error("the " + this.field.getName()
                    + " element can't be found and the time(" + String.valueOf(timeout) + ") is out");
            throw new RuntimeException("the " + this.field.getName()
                    + " element can't be found and the time(" + String.valueOf(timeout) + ") is out");
        }

        if (cachedElements == null || !((CacheableLocator) locator).isLookUpCached()) {
            cachedElements = realElements;
            cachedWidgets.clear();

            for (WebElement element : cachedElements) {
                ContentType type = getCurrentContentType(element);
                Class<?>[] params = new Class<?>[]{instantiationMap.get(type).getParameterTypes()[0]};
                cachedWidgets.add(ProxyFactory.getEnhancedProxy(declaredType, params, new Object[]{element},
                        new WidgetInterceptor(null, driver, element, instantiationMap, duration, field)));
            }
        }
        try {
            return method.invoke(cachedWidgets, args);
        } catch (Throwable t) {
            throw ThrowableUtil.extractReadableException(t);
        }
    }
}

