package com.qa.framework.ioc;

import com.library.common.ReflectHelper;
import com.qa.framework.cache.DriverCache;
import com.qa.framework.common.Driver;
import com.qa.framework.config.PropConfig;
import com.qa.framework.config.Value;
import com.qa.framework.ioc.annotation.Page;
import com.qa.framework.pagefactory.PageFactory;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.qa.framework.ioc.IocHelper.findImplementClass;

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
    public static void initFields(Object obj) {
        Class<?> clazz = obj.getClass();
        boolean isAbs = Modifier.isAbstract(clazz.getModifiers());
        while (clazz != Object.class && !isAbs) {
            proxyFields(obj, clazz);
            clazz = clazz.getSuperclass();
            isAbs = Modifier.isAbstract(clazz.getModifiers());
        }
    }

    private static void proxyFields(Object obj, Class<?> clazz) {
        do {
            Field[] fields = clazz.getDeclaredFields();
            fillForFields(obj, fields);

            if (clazz.getSuperclass() == null) {
                return;
            }
            clazz = clazz.getSuperclass();

        } while (true);
    }

    private static void fillForFields(Object obj, Field[] fields) {
        for (Field field : fields) {
            if (field.getAnnotation(Autowired.class) != null) {
                Object proxy = IocContainer.getIocObject(field.getType());
                if (proxy == null) {
                    logger.debug(field.getName() + " is not existed in IOC Container");
                    Class<?> clazz = field.getType();
                    if (clazz.getSimpleName().equals("WebDriver")) {
                        proxy = DriverCache.get();
                    } else {
                        Class<?> implementClass = null;
                        if (Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
                            implementClass = findImplementClass(clazz);
                        } else {
                            implementClass = clazz;
                        }

                        try {
                            if (implementClass != null) {
                                proxy = implementClass.newInstance();
                            }
                        } catch (InstantiationException | IllegalAccessException e) {
                            logger.error(e.toString(), e);
                        }
                    }

                } else {
                    logger.debug(field.getName() + " is existed in IOC Container");
                }
                try {
                    field.setAccessible(true);
                    field.set(obj, proxy);
                    if (field.getType().isAnnotationPresent(Service.class) || field.getType().isAnnotationPresent(Page.class)) {
                        initFields(proxy);
                        if (field.getType().isAnnotationPresent(Page.class)) {
                            Driver driver = new Driver(DriverCache.get());
                            if (!driver.isMobilePlat()) {
                                DriverCache.get().manage().timeouts().pageLoadTimeout(120000, TimeUnit.MILLISECONDS);
                            }
                            PageFactory.initElements(DriverCache.get(), proxy);
                        }
                    }
                } catch (IllegalAccessException e) {
                    logger.error(e.toString(), e);
                }
            } else if (field.getAnnotation(Value.class) != null) {
                Value value = field.getAnnotation(Value.class);
                String fieldKey = value.value();
                Properties props = PropConfig.getProps();
                if (props.getProperty(fieldKey) != null) {
                    String fieldValue = props.getProperty(fieldKey);
                    field.setAccessible(true);
                    ReflectHelper.setValue(obj, field, fieldValue);
                }
            }
        }
    }
}
