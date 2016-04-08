package com.qa.framework.library.httpclient;

import org.apache.http.client.CookieStore;

/**
 * Created by apple on 15/11/9.
 */
public class CookieCache {
    private static ThreadLocal<CookieStore> cookieStoreThreadLocal = new ThreadLocal<CookieStore>();

    /**
     * Set.
     *
     * @param cookie the cookie
     */
    public static void set(CookieStore cookie) {
        cookieStoreThreadLocal.set(cookie);
    }

    /**
     * Get cookie store.
     *
     * @return the cookie store
     */
    public static CookieStore get() {
        return cookieStoreThreadLocal.get();
    }

    /**
     * Clear.
     */
    public static void clear() {
        set(null);
    }

}
