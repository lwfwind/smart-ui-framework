package com.qa.framework.pagefactory.web.interceptor;

import com.qa.framework.cache.DriverCache;
import com.qa.framework.cache.MethodCache;
import com.qa.framework.common.Alert;
import com.qa.framework.common.ScreenShot;
import com.qa.framework.common.Sleeper;
import com.qa.framework.pagefactory.web.Element;
import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * The type Sub element handler.
 */
public class SubElementHandler implements InvocationHandler {
    /**
     * The Alert.
     */
    protected final Alert alert;
    private final Sleeper sleeper;
    private final Object element;
    private final String logicParentElementName;
    private final int num;
    private final WebDriver driver;
    private final Field field;
    /**
     * The Logger.
     */
    protected Logger logger = Logger.getLogger(SubElementHandler.class);

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
                logger.info(logicParentElementName + "_" + num + " click");
                this.sleeper.sleep(500);
                if (driver.getWindowHandles().size() > previousWindowsCount) {
                    this.alert.selectLastOpenedWindow();
                }
                return ret;
            }
            if (paras != null) {
                logger.info(logicParentElementName + "_" + num + " " + method.getName() + " " + Arrays.deepToString(paras));
            } else {
                logger.info(logicParentElementName + "_" + num + " " + method.getName());
            }
            return method.invoke(element, paras);
        } catch (InvocationTargetException e) {
            // Unwrap the underlying exception
            if (e.getCause().toString().contains("clickable")) {
                logger.info(e.getCause());
                this.sleeper.sleep(5000);
                wapperElement.click();
                return "action.click";
            } else {
                throw e.getCause();
            }
        }
    }
}