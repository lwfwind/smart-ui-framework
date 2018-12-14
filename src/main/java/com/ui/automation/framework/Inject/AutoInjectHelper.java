package com.ui.automation.framework.Inject;

import com.google.gson.Gson;
import com.ui.automation.framework.Inject.annotation.Page;
import com.ui.automation.framework.SpringContext;
import com.ui.automation.framework.cache.DriverCache;
import com.ui.automation.framework.common.Driver;
import com.ui.automation.framework.pagefactory.PageFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

public class AutoInjectHelper {
    /**
     * The constant logger.
     */
    protected final static Logger logger = Logger.getLogger(AutoInjectHelper.class);

    /**
     * Init fields.
     *
     * @param obj the obj
     */
    public static void initFields(Object obj) throws Exception {
        Class<?> clazz = obj.getClass();
        boolean isAbs = Modifier.isAbstract(clazz.getModifiers());
        while (clazz != Object.class && !isAbs) {
            proxyFields(obj, clazz);
            clazz = clazz.getSuperclass();
            isAbs = Modifier.isAbstract(clazz.getModifiers());
        }
    }

    private static void proxyFields(Object obj, Class<?> clazz) throws Exception {
        do {
            Field[] fields = clazz.getDeclaredFields();
            fillForFields(obj, fields);

            if (clazz.getSuperclass() == null) {
                return;
            }
            clazz = clazz.getSuperclass();

        } while (true);
    }

    private static String serializeObject(Object o) {
        Gson gson = new Gson();
        return gson.toJson(o);
    }

    private static Object deserializeObject(String s, Object o) {
        Gson gson = new Gson();
        return gson.fromJson(s, o.getClass());
    }

    private static Object cloneObject(Object o) {
        String s = serializeObject(o);
        return deserializeObject(s, o);
    }

    private static Object getBeanFromSpringContext(Field field) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        ApplicationContext applicationContext = SpringContext.getApplicationContext();
        if (applicationContext != null) {
            String[] beanNames = applicationContext.getBeanDefinitionNames();
            for (String beanName : beanNames) {
                Object bean = SpringContext.getBean(beanName);
                if (bean.getClass().getName().equals(field.getType().getName())) {
                    return cloneObject(bean);
                } else {
                    Class<?>[] beanImplementClasses = bean.getClass().getInterfaces();
                    for (Class<?> beanImplementClass : beanImplementClasses) {
                        if (beanImplementClass.getName().equals(field.getType().getName())) {
                            return cloneObject(bean);
                        }
                    }
                }
            }
        }
        return null;
    }

    private static void fillForFields(Object obj, Field[] fields) throws Exception {
        for (Field field : fields) {
            Object proxy = null;
            logger.debug(field.getName() + " is not existed in IOC Container");
            Class<?> clazz = field.getType();
            if (clazz.getName().startsWith("java.lang")) {
                continue;
            }
            if (clazz.getSimpleName().equals("WebDriver")) {
                proxy = DriverCache.get();
                if (proxy != null) {
                    field.setAccessible(true);
                    field.set(obj, proxy);
                }
            } else {
                if (field.isAnnotationPresent(Autowired.class)) {
                    if (field.getType().isAnnotationPresent(Service.class) || field.getType().isAnnotationPresent(Page.class)) {
                        proxy = getBeanFromSpringContext(field);
                        if (proxy != null) {
                            field.setAccessible(true);
                            field.set(obj, proxy);
                        }
                        if (proxy != null) {
                            initFields(proxy);
                            if (field.getType().isAnnotationPresent(Page.class)) {
                                Driver driver = new Driver(DriverCache.get());
                                if (!driver.isMobilePlat()) {
                                    DriverCache.get().manage().timeouts().pageLoadTimeout(120000, TimeUnit.MILLISECONDS);
                                }
                                PageFactory.initElements(DriverCache.get(), proxy);
                            }
                        }
                    }
                }
            }
        }
    }
}
