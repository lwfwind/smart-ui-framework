package com.qa.framework.library.base;

import org.apache.commons.lang.ArrayUtils;

/**
 * 数组操作工具类
 */
public class ArrayUtil {

    /**
     * 判断数组是否非空
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isNotEmpty(Object[] array) {
        return !ArrayUtils.isEmpty(array);
    }

    /**
     * 判断数组是否为空
     *
     * @param array the array
     * @return the boolean
     */
    public static boolean isEmpty(Object[] array) {
        return ArrayUtils.isEmpty(array);
    }

    /**
     * 连接数组
     *
     * @param array1 the array 1
     * @param array2 the array 2
     * @return the object [ ]
     */
    public static Object[] concat(Object[] array1, Object[] array2) {
        return ArrayUtils.addAll(array1, array2);
    }

    /**
     * 判断对象是否在数组中
     *
     * @param <T>   the type parameter
     * @param array the array
     * @param obj   the obj
     * @return the boolean
     */
    public static <T> boolean contains(T[] array, T obj) {
        return ArrayUtils.contains(array, obj);
    }
}
