package com.qa.framework.pagefactory.mobile;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidElement;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.IOSElement;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kcgw001 on 2016/4/5.
 */
public class ProxyUtil {

    private final static Map<Class<? extends SearchContext>, Class<? extends WebElement>> elementRuleMap =
            new HashMap<Class<? extends SearchContext>, Class<? extends WebElement>>() {
                private static final long serialVersionUID = 1L;

                {
                    put(AndroidDriver.class, AndroidElement.class);
                    put(IOSDriver.class, IOSElement.class);
                }
            };

    public static Class<?> getTypeForProxy(Class<? extends SearchContext> driverClass) {
        Iterable<Map.Entry<Class<? extends SearchContext>, Class<? extends WebElement>>> rules = elementRuleMap.entrySet();
        //it will return MobileElement subclass when here is something
        for (Map.Entry<Class<? extends SearchContext>, Class<? extends WebElement>> e : rules) {
            //that extends AppiumDriver or MobileElement
            if (e.getKey().isAssignableFrom(driverClass)) {
                return e.getValue();
            }
        } //it is compatible with desktop browser. So at this case it returns RemoteWebElement.class
        return RemoteWebElement.class;
    }
}
