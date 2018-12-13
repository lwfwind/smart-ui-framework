package com.ui.automation.framework.pagefactory.mobile;

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

import com.ui.automation.framework.pagefactory.mobile.interceptor.ElementInterceptor;
import com.ui.automation.framework.pagefactory.mobile.interceptor.ElementListInterceptor;
import com.ui.automation.framework.pagefactory.mobile.interceptor.WidgetInterceptor;
import com.ui.automation.framework.pagefactory.mobile.interceptor.WidgetListInterceptor;
import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchableElement;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSElement;
import io.appium.java_client.pagefactory.TimeOutDuration;
import io.appium.java_client.pagefactory.Widget;
import io.appium.java_client.pagefactory.bys.ContentType;
import io.appium.java_client.pagefactory.locator.CacheableLocator;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;
import org.openqa.selenium.support.pagefactory.DefaultFieldDecorator;
import org.openqa.selenium.support.pagefactory.ElementLocator;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static io.appium.java_client.pagefactory.utils.ProxyFactory.getEnhancedProxy;
import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.*;

/**
 * Default decorator for use with PageFactory. Will decorate 1) all of the
 * WebElement fields and 2) List of WebElement that have
 * {@literal @AndroidFindBy}, {@literal @AndroidFindBys}, or
 * {@literal @iOSFindBy/@iOSFindBys} annotation with a proxy that locates the
 * elements using the passed in ElementLocatorFactory.
 * Please pay attention: fields of {@link WebElement}, {@link RemoteWebElement},
 * {@link MobileElement}, {@link AndroidElement} and {@link IOSElement} are allowed
 * to use with this decorator
 */
public class AppiumFieldDecorator implements FieldDecorator {

    private static final List<Class<? extends WebElement>> availableElementClasses =
            new ArrayList<Class<? extends WebElement>>() {
                private static final long serialVersionUID = 1L;

                {
                    add(WebElement.class);
                    add(RemoteWebElement.class);
                    add(MobileElement.class);
                    add(TouchableElement.class);
                    add(AndroidElement.class);
                    add(IOSElement.class);
                }

            };

    /**
     * The constant DEFAULT_IMPLICITLY_WAIT_TIMEOUT.
     */
    public static long DEFAULT_IMPLICITLY_WAIT_TIMEOUT = 1;
    /**
     * The constant DEFAULT_TIMEUNIT.
     */
    public static TimeUnit DEFAULT_TIMEUNIT = TimeUnit.SECONDS;
    private final WebDriver originalDriver;
    private final DefaultFieldDecorator defaultElementFieldDecoracor;
    private final AppiumElementLocatorFactory widgetLocatorFactory;
    private final String platform;
    private final String automation;
    private final TimeOutDuration timeOutDuration;
    private Field field;

    /**
     * Instantiates a new Appium field decorator.
     *
     * @param context               the context
     * @param implicitlyWaitTimeOut the implicitly wait time out
     * @param timeUnit              the time unit
     */
    public AppiumFieldDecorator(SearchContext context, long implicitlyWaitTimeOut, TimeUnit timeUnit) {
        this(context, new TimeOutDuration(implicitlyWaitTimeOut, timeUnit));
    }

    /**
     * Instantiates a new Appium field decorator.
     *
     * @param context         the context
     * @param timeOutDuration the time out duration
     */
    public AppiumFieldDecorator(SearchContext context, TimeOutDuration timeOutDuration) {
        this.originalDriver = unpackWebDriverFromSearchContext(context);
        platform = getPlatform(originalDriver);
        automation = getAutomation(originalDriver);
        this.timeOutDuration = timeOutDuration;

        defaultElementFieldDecoracor = new DefaultFieldDecorator(
                new AppiumElementLocatorFactory(context, timeOutDuration, originalDriver,
                        new DefaultElementByBuilder(platform, automation))) {
            @Override
            protected WebElement proxyForLocator(ClassLoader ignored, ElementLocator locator) {
                return proxyForAnElement(locator);
            }

            @Override
            @SuppressWarnings("unchecked")
            protected List<WebElement> proxyForListLocator(ClassLoader ignored, ElementLocator locator) {
                ElementListInterceptor elementInterceptor = new ElementListInterceptor(locator, originalDriver, field);
                return getEnhancedProxy(ArrayList.class,
                        elementInterceptor);
            }

            @Override
            protected boolean isDecoratableList(Field field) {
                if (!List.class.isAssignableFrom(field.getType())) {
                    return false;
                }

                Type genericType = field.getGenericType();
                if (!(genericType instanceof ParameterizedType)) {
                    return false;
                }

                Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];

                boolean result = false;
                for (Class<? extends WebElement> webElementClass :
                        availableElementClasses) {
                    if (!webElementClass.equals(listType)) {
                        continue;
                    }
                    result = true;
                    break;
                }
                return result;
            }
        };

        widgetLocatorFactory = new AppiumElementLocatorFactory(context, timeOutDuration, originalDriver,
                new WidgetByBuilder(platform, automation));
    }

    /**
     * Instantiates a new Appium field decorator.
     *
     * @param context the context
     */
    public AppiumFieldDecorator(SearchContext context) {
        this(context, DEFAULT_IMPLICITLY_WAIT_TIMEOUT, DEFAULT_TIMEUNIT);
    }

    public Object decorate(ClassLoader ignored, Field field) {
        this.field = field;
        Object result = defaultElementFieldDecoracor.decorate(ignored, field);
        if (result != null) {
            return result;
        }

        return decorateWidget(field);
    }

    @SuppressWarnings("unchecked")
    private Object decorateWidget(Field field) {
        Class<?> type = field.getType();
        if (!Widget.class.isAssignableFrom(type) && !List.class.isAssignableFrom(type)) {
            return null;
        }

        Class<? extends Widget> widgetType;
        boolean isAlist = false;
        if (List.class.isAssignableFrom(type)) {
            isAlist = true;
            Type genericType = field.getGenericType();
            if (!(genericType instanceof ParameterizedType)) {
                return null;
            }

            Type listType = ((ParameterizedType) genericType).getActualTypeArguments()[0];

            if (ParameterizedType.class.isAssignableFrom(listType.getClass())) {
                listType = ((ParameterizedType) listType).getRawType();
            }

            if (!Widget.class.isAssignableFrom((Class) listType)) {
                return null;
            }

            widgetType = Class.class.cast(listType);
        } else {
            widgetType = (Class<? extends Widget>) field.getType();
        }

        CacheableLocator locator = widgetLocatorFactory.createLocator(field);
        Map<ContentType, Constructor<? extends Widget>> map =
                OverrideWidgetReader.read(widgetType, field, platform, automation);

        if (isAlist) {
            return getEnhancedProxy(ArrayList.class,
                    new WidgetListInterceptor(locator, originalDriver, map, widgetType, timeOutDuration, field));
        }

        Constructor<? extends Widget> constructor = WidgetConstructorUtil.findConvenientConstructor(widgetType);
        return getEnhancedProxy(widgetType, new Class[]{constructor.getParameterTypes()[0]},
                new Object[]{proxyForAnElement(locator)}, new WidgetInterceptor(locator, originalDriver, null, map,
                        timeOutDuration, field));
    }

    private WebElement proxyForAnElement(ElementLocator locator) {
        ElementInterceptor elementInterceptor = new ElementInterceptor(locator, originalDriver, field);
        return (WebElement) getEnhancedProxy(ProxyUtil.getTypeForProxy(originalDriver.getClass()), elementInterceptor);
    }
}

