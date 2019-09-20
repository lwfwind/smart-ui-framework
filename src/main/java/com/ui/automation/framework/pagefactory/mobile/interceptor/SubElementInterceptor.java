package com.ui.automation.framework.pagefactory.mobile.interceptor;

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

import com.ui.automation.framework.webdriver.Sleeper;
import com.ui.automation.framework.pagefactory.mobile.ThrowableUtil;
import io.appium.java_client.MobileElement;
import lombok.extern.slf4j.Slf4j;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Intercepts requests to {@link MobileElement}
 */
@Slf4j
public class SubElementInterceptor implements MethodInterceptor {
    /**
     * The Element.
     */
    protected final WebElement element;
    /**
     * The Driver.
     */
    protected final WebDriver driver;
    /**
     * The Field.
     */
    protected final Field field;
    /**
     * The Num.
     */
    protected final int num;
    private final Sleeper sleeper;
    /**
     * Instantiates a new Sub element interceptor.
     *
     * @param element the element
     * @param driver  the driver
     * @param field   the field
     * @param num     the num
     */
    public SubElementInterceptor(WebElement element, WebDriver driver, Field field, int num) {
        this.element = element;
        this.driver = driver;
        this.field = field;
        this.num = num;
        this.sleeper = new Sleeper();
    }

    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy proxy) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return proxy.invokeSuper(obj, args);
        }

        if (args != null && args.length > 0) {
            log.info(this.field.getName() + "_" + num + " " + method.getName() + " " + Arrays.deepToString(args));
        } else {
            log.info(this.field.getName() + "_" + num + " " + method.getName());
        }

        try {
            return method.invoke(element, args);
        } catch (Throwable t) {
            throw ThrowableUtil.extractReadableException(t);
        }
    }

}
