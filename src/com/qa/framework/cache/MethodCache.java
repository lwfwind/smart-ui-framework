package com.qa.framework.cache;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by kcgw001 on 2016/2/2.
 */
public class MethodCache {
    private static ConcurrentHashMap<String, Integer> counter = new ConcurrentHashMap<String, Integer>();
    private static ThreadLocal<String> methodCache = new ThreadLocal<String>();
    private static Vector<String> caches = new Vector<String>();

    public static void set(String methodName) {
        String convMethodName = "";
        if (caches.contains(methodName)) {
            convMethodName = methodName + "_" + getCounter(methodName);
        } else {
            convMethodName = methodName;
        }
        caches.add(convMethodName);
        methodCache.set(convMethodName);
    }

    public static int getCounter(String methodName) {
        if (counter.get(methodName) == null) {
            counter.put(methodName, 1);
            return 1;
        } else {
            int count = counter.get(methodName) + 1;
            counter.put(methodName, count);
            return count;
        }
    }

    public static String getCurrentMethodName() {
        return methodCache.get();
    }
}
