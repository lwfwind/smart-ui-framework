package com.ui.automation.framework.pagefactory;

import com.ui.automation.framework.pagefactory.mobile.AppiumFieldDecorator;
import com.ui.automation.framework.pagefactory.web.ElementDecorator;
import io.appium.java_client.remote.MobilePlatform;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.pagefactory.DefaultElementLocatorFactory;
import org.openqa.selenium.support.pagefactory.FieldDecorator;

import java.lang.reflect.Field;

import static io.appium.java_client.pagefactory.utils.WebDriverUnpackUtility.getPlatform;


/**
 * Factory class to make using Page Objects simpler and easier.
 */
@Slf4j
public class PageFactory {

    /**
     * Init elements.
     *
     * @param driver the driver
     * @param page   the page
     */
    public static void initElements(WebDriver driver, Object page) {
        Class<?> pageClass = page.getClass();
        while (pageClass != Object.class) {
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
                    log.error(e.getMessage(), e);
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
