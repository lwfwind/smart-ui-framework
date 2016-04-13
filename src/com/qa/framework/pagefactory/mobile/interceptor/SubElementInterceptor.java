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

import com.qa.framework.library.webdriver.Action;
import com.qa.framework.pagefactory.mobile.ThrowableUtil;
import io.appium.java_client.MobileElement;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Intercepts requests to {@link MobileElement}
 */
public class SubElementInterceptor implements MethodInterceptor {
    protected final WebElement element;
    protected final WebDriver driver;
    protected final Field field;
    protected final Action action;
    protected final int num;
    protected Logger logger = Logger.getLogger(SubElementInterceptor.class);

    public SubElementInterceptor(WebElement element, WebDriver driver, Field field, int num) {
        this.element = element;
        this.driver = driver;
        this.field = field;
        this.num = num;
        this.action = new Action(driver);
    }

    public Object intercept(Object obj, Method method, Object[] args,
                            MethodProxy proxy) throws Throwable {
        if (args != null && args.length > 0) {
            logger.info(this.field.getName() + "_" + num + " " + method.getName() + " " + Arrays.deepToString(args));
        } else {
            logger.info(this.field.getName() + "_" + num + " " + method.getName());
        }

        try {
            return method.invoke(element, args);
        } catch (Throwable t) {
            throw ThrowableUtil.extractReadableException(t);
        }
    }

}
