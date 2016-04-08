package com.qa.framework.cache;

import com.qa.framework.bean.Method;

import java.util.HashMap;

/**
 * Created by kcgw001 on 2016/2/2.
 */
public class ResultCache {
    private static ThreadLocal<HashMap<Integer, Method>> ResultCache = new ThreadLocal<>();

    public static void set(HashMap<Integer, Method> hashMap) {
        ResultCache.set(hashMap);
    }

    public static HashMap<Integer, Method> get() {
        return ResultCache.get();
    }
}
