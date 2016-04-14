package com.qa.framework.pagefactory;

import com.qa.framework.PageBase;
import com.qa.framework.pagefactory.mobile.AppiumFieldDecorator;
import com.qa.framework.pagefactory.web.ElementDecorator;
import io.appium.java_client.remote.MobilePlatform;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import java.lang.reflect.Field;

import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.getPlatform;


/**
 * Factory class to make using Page Objects simpler and easier.
 */
public class PageFactory {
    private final static Logger logger = Logger
            .getLogger(PageFactory.class);

    /**
     * Init elements.
     *
     * @param driver the driver
     * @param page   the page
     */
    public static void initElements(WebDriver driver, Object page) {
        Class<?> pageClass = page.getClass();
        while (pageClass != Object.class && pageClass != PageBase.class) {
            proxyFields(driver, page, pageClass);
            pageClass = pageClass.getSuperclass();
        }
    }

    private static void proxyFields(WebDriver driver, Object page, Class<?> pageClass) {
        Field[] fields = pageClass.getDeclaredFields();
        for (Field field : fields) {
            Object proxy = null;
            FieldDecorator decorator = null;
            if (getPlatform(driver).equals(MobilePlatform.ANDROID) || getPlatform(driver).equals(MobilePlatform.IOS) || getPlatform(driver).equals(MobilePlatform.FIREFOX_OS)) {
                decorator = new AppiumFieldDecorator(driver);
            } else {
                decorator = new ElementDecorator(new DefaultElementLocatorFactory(driver));
            }
            proxy = decorator.decorate(page.getClass().getClassLoader(), field);
            if (proxy != null) {
                try {
                    field.setAccessible(true);
                    field.set(page, proxy);
                } catch (IllegalAccessException e) {
                    logger.error(e.toString(), e);
                }
            }
        }
    }

    /**
     * Similar to the other "initElements" methods, but takes an {@link FieldDecorator} which is used
     * for decorating each of the fields.
     *
     * @param decorator the decorator to use
     * @param page      The object to decorate the fields of
     */
    public static void initElements(FieldDecorator decorator, Object page) {
        Class<?> proxyIn = page.getClass();
        while (proxyIn != Object.class) {
            proxyFields(decorator, page, proxyIn);
            proxyIn = proxyIn.getSuperclass();
        }
    }

    private static void proxyFields(FieldDecorator decorator, Object page, Class<?> proxyIn) {
        Field[] fields = proxyIn.getDeclaredFields();
        for (Field field : fields) {
            Object value = decorator.decorate(page.getClass().getClassLoader(), field);
            if (value != null) {
                try {
                    field.setAccessible(true);
                    field.set(page, value);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
