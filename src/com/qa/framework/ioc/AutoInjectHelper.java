package com.qa.framework.ioc;

import com.qa.framework.ioc.annotation.AutoInject;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import static com.qa.framework.ioc.IocHelper.findImplementClass;

/**
 * Created by kcgw001 on 2016/3/14.
 */
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

    private static void fillForFields(Object obj, Field[] fields) {
        for (Field field : fields) {
            if (field.getAnnotation(AutoInject.class) != null) {
                Object proxy = IocContainer.getIocObject(field.getType());
                if (proxy == null) {
                    logger.info(field.getName() + " is not existed in IOC Container");
                    Class<?> implementClass = findImplementClass(field.getType());
                    try {
                        if (implementClass != null) {
                            proxy = implementClass.newInstance();
                        }
                    } catch (InstantiationException | IllegalAccessException e) {
                        logger.error(e.toString(), e);
                    }
                } else {
                    logger.info(field.getName() + " is existed in IOC Container");
                }
                try {
                    field.setAccessible(true);
                    field.set(obj, proxy);
                } catch (IllegalAccessException e) {
                    logger.error(e.toString(), e);
                }
            }
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
}
