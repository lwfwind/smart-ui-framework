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

import com.qa.framework.pagefactory.mobile.ThrowableUtil;
import com.qa.framework.library.webdriver.Action;
import io.appium.java_client.pagefactory.TimeOutDuration;
import io.appium.java_client.pagefactory.Widget;
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

import static com.qa.framework.pagefactory.TimeOutOfFindProcessor.getTimeOutOfFind;
import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.getCurrentContentType;


public class WidgetListInterceptor implements MethodInterceptor {

    private final Map<ContentType, Constructor<? extends Widget>> instantiationMap;
    private final List<Widget> cachedWidgets = new ArrayList<>();
    private final Class<? extends Widget> declaredType;
    private final TimeOutDuration duration;
    private final WebDriver driver;
    private List<WebElement> cachedElements;
    protected final ElementLocator locator;
    protected Logger logger = Logger.getLogger(WidgetListInterceptor.class);
    protected final Field field;
    protected final Action action;

    public WidgetListInterceptor(CacheableLocator locator, WebDriver driver, Map<ContentType, Constructor<? extends Widget>> instantiationMap,
                          Class<? extends Widget> declaredType, TimeOutDuration duration, Field field) {
        this.locator = locator;
        this.instantiationMap = instantiationMap;
        this.declaredType = declaredType;
        this.duration = duration;
        this.driver = driver;
        this.field = field;
        this.action = new Action(driver);
    }

    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy proxy) throws Throwable {
        if(Object.class.equals(method.getDeclaringClass())) {
            return proxy.invokeSuper(obj, args);
        }

        List<WebElement> realElements = null;
        int timeout = getTimeOutOfFind(field);
        long end = System.currentTimeMillis() + timeout;
        while (System.currentTimeMillis() < end) {
            realElements = locator.findElements();
            if (realElements != null && realElements.size() > 0) {
                break;
            }
            this.action.pause(500);
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
                        new WidgetInterceptor(null, driver, element, instantiationMap, duration,field)));
            }
        }
        try {
            return method.invoke(cachedWidgets, args);
        } catch (Throwable t) {
            throw ThrowableUtil.extractReadableException(t);
        }
    }
}

