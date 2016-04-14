package com.qa.framework.cache;

import org.openqa.selenium.WebElement;

/**
 * Created by kcgw001 on 2016/2/2.
 */
public class ElementCache {
    private static ThreadLocal<WebElement> ElementCache = new ThreadLocal<WebElement>();

    /**
     * Set.
     *
     * @param element the element
     */
    public static void set(WebElement element) {
        ElementCache.set(element);
    }

    /**
     * Get web element.
     *
     * @return the web element
     */
    public static WebElement get() {
        return ElementCache.get();
    }
}
