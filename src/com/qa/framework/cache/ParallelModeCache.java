package com.qa.framework.cache;

/**
 * Created by kcgw001 on 2016/2/2.
 */
public class ParallelModeCache {
    private static String Parallel_mode = "";


    /**
     * Set.
     *
     * @param mode the mode
     */
    public static void set(String mode) {
        Parallel_mode = mode;
    }

    /**
     * Get string.
     *
     * @return the string
     */
    public static String get() {
        return Parallel_mode;
    }
}
