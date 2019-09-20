package com.ui.automation.framework.pagefactory.web.interceptor;

import com.ui.automation.framework.cache.DriverCache;
import com.ui.automation.framework.cache.MethodCache;
import com.ui.automation.framework.webdriver.Alert;
import com.ui.automation.framework.webdriver.ScreenShot;
import com.ui.automation.framework.webdriver.Sleeper;
import com.ui.automation.framework.webdriver.Window;
import com.ui.automation.framework.pagefactory.web.Element;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * The type Sub element handler.
 */
@Slf4j
public class SubElementHandler implements InvocationHandler {
    /**
     * The Alert.
     */
    private final Alert alert;
    private final Window window;
    private final Sleeper sleeper;
    private final Object element;
    private final String logicParentElementName;
    private final int num;
    private final WebDriver driver;
    private final Field field;

    /**
     * Instantiates a new Sub element handler.
     *
     * @param element the element
     * @param field   the field
     * @param num     the num
     */
    public SubElementHandler(Object element, Field field, int num) {
        this.element = element;
        this.logicParentElementName = field.getName();
        this.field = field;
        this.num = num;
        this.driver = DriverCache.get();
        this.alert = new Alert(driver);
        this.window = new Window(driver);
        this.sleeper = new Sleeper();
    }

    public Object invoke(Object object, Method method, Object[] paras) throws Throwable {
        if ("getWrappedElement".equals(method.getName())) {
            return element;
        }
        int previousWindowsCount = driver.getWindowHandles().size();
        Element wapperElement = (Element) element;
        try {
            if (method.getName().equals("click") || method.getName().equals("sendKeys")) {
                wapperElement.scrollIntoView(false);
                this.sleeper.sleep(50);
                wapperElement.highLight();
                this.sleeper.sleep(50);
            }

            if (method.getName().equals("click")) {
                String currMethodName = MethodCache.getCurrentMethodName();
                ScreenShot.captureAction(driver, currMethodName, logicParentElementName + "_" + num);
                Object ret = method.invoke(element, paras);
                log.info(logicParentElementName + "_" + num + " click");
                this.sleeper.sleep(500);
                if (driver.getWindowHandles().size() > previousWindowsCount) {
                    this.window.selectLastOpenedWindow();
                }
                return ret;
            }
            if (paras != null) {
                log.info(logicParentElementName + "_" + num + " " + method.getName() + " " + Arrays.deepToString(paras));
            } else {
                log.info(logicParentElementName + "_" + num + " " + method.getName());
            }
            return method.invoke(element, paras);
        } catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            if (e.getCause().toString().contains("clickable")) {
                log.info(String.valueOf(e.getCause()));
                this.sleeper.sleep(5000);
                wapperElement.click();
                return "action.click";
            } else {
                throw e.getCause();
            }
        }
    }
}
