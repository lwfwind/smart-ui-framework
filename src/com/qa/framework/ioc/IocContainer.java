package com.qa.framework.ioc;

import com.qa.framework.ioc.annotation.Page;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IocContainer {
    private static final Logger logger = Logger.getLogger(IocContainer.class);
    private static final Map<Class<?>, Object> container = new HashMap<Class<?>, Object>();

    static {
        try {
            // 获取应用包路径下所有的类
            List<Class<?>> classList = ClassFinder.getClassList();
            for (Class<?> cls : classList) {
                if (cls.isAnnotationPresent(Service.class)
                        || cls.isAnnotationPresent(Page.class)) {
                    Object instance = cls.newInstance();
                    //container.put(cls, instance);
                    //logger.info("Add the class " + cls.getName() + " ioc container");
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("初始化类出错！" + e.getCause());
        }
    }

    /**
     * Gets ioc map.
     *
     * @return the ioc map
     */
    public static Map<Class<?>, Object> getIocMap() {
        return container;
    }


    /**
     * Gets ioc object.
     *
     * @param <T> the type parameter
     * @param cls the cls
     * @return the ioc object
     */
    @SuppressWarnings("unchecked")
    public static <T> T getIocObject(Class<T> cls) {
        if (!container.containsKey(cls)) {
            return null;
        }
        return (T) container.get(cls);
    }

    /**
     * Sets ioc object.
     *
     * @param cls the cls
     * @param obj the obj
     */
    public static void setIocObject(Class<?> cls, Object obj) {
        container.put(cls, obj);
    }
}
