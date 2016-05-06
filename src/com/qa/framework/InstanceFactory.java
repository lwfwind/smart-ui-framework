package com.qa.framework;

import com.qa.framework.ioc.ClassScanner;
import com.qa.framework.ioc.impl.DefaultClassScanner;
import com.qa.framework.library.base.ReflectHelper;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 实例工厂
 */
public class InstanceFactory {

    /**
     * 用于缓存对应的实例
     */
    private static final Map<String, Object> cache = new ConcurrentHashMap<String, Object>();

    /**
     * ClassScanner
     */
    private static final String CLASS_SCANNER = "class_scanner";


    /**
     * 获取 ClassScanner
     *
     * @return the class scanner
     */
    public static ClassScanner getClassScanner() {
        return getInstance(CLASS_SCANNER, DefaultClassScanner.class);
    }


    /**
     * Gets instance.
     *
     * @param <T>              the type parameter
     * @param cacheKey         the cache key
     * @param defaultImplClass the default impl class
     * @return the instance
     */
    @SuppressWarnings("unchecked")
    public static <T> T getInstance(String cacheKey, Class<T> defaultImplClass) {
        // 若缓存中存在对应的实例，则返回该实例
        if (cache.containsKey(cacheKey)) {
            return (T) cache.get(cacheKey);
        }
        // 通过反射创建该实现类对应的实例
        T instance = ReflectHelper.newInstance(defaultImplClass.getName());
        // 若该实例不为空，则将其放入缓存
        if (instance != null) {
            cache.put(cacheKey, instance);
        }
        // 返回该实例
        return instance;
    }
}
