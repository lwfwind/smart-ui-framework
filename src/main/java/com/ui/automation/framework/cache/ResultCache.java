package com.ui.automation.framework.cache;

import com.ui.automation.framework.bean.Method;

import java.util.HashMap;

/**
 * Created by kcgw001 on 2016/2/2.
 */
public class ResultCache {
    private static ThreadLocal<HashMap<Integer, Method>> ResultCache = new ThreadLocal<>();

    /**
     * Set.
     *
     * @param hashMap the hash map
     */
    public static void set(HashMap<Integer, Method> hashMap) {
        ResultCache.set(hashMap);
    }

    /**
     * Get hash map.
     *
     * @return the hash map
     */
    public static HashMap<Integer, Method> get() {
        return ResultCache.get();
    }
}
