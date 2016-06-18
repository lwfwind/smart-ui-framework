package com.qa.framework.cache;

public class PackageCache {
    private static String packageName = null;

    /**
     * Set.
     *
     * @param pName the package Name
     */
    public static void set(String pName) {
        packageName = pName;
    }

    /**
     * Get packageName
     *
     * @return the packageName
     */
    public static String get() {
        return packageName;
    }
}
