package com.qa.framework.library.base;

import org.apache.log4j.Logger;

import java.net.URL;

/**
 * Created by kcgw001 on 2016/4/12.
 */
public class ClassHelper {

    private final static Logger logger = Logger
            .getLogger(ClassHelper.class);

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        logger.info(getClassLoader());
        logger.info(getClassPath());
    }

    /**
     * 获取类加载器
     *
     * @return the class loader
     */
    public static ClassLoader getClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    /**
     * 获取类路径
     *
     * @return the class path
     */
    public static String getClassPath() {
        String classpath = "";
        URL resource = getClassLoader().getResource("");
        if (resource != null) {
            classpath = resource.getPath();
        }
        return classpath;
    }

    /**
     * 加载类（将自动初始化）
     *
     * @param className the class name
     * @return the class
     */
    public static Class<?> loadClass(String className) {
        return loadClass(className, true);
    }

    /**
     * 加载类
     *
     * @param className     the class name
     * @param isInitialized the is initialized
     * @return the class
     */
    public static Class<?> loadClass(String className, boolean isInitialized) {
        Class<?> cls;
        try {
            cls = Class.forName(className, isInitialized, getClassLoader());
        } catch (ClassNotFoundException e) {
            logger.error("加载类出错！", e);
            throw new RuntimeException(e);
        }
        return cls;
    }

    /**
     * 是否为 int 类型（包括 Integer 类型）
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isInt(Class<?> type) {
        return type.equals(int.class) || type.equals(Integer.class);
    }

    /**
     * 是否为 long 类型（包括 Long 类型）
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isLong(Class<?> type) {
        return type.equals(long.class) || type.equals(Long.class);
    }

    /**
     * 是否为 double 类型（包括 Double 类型）
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isDouble(Class<?> type) {
        return type.equals(double.class) || type.equals(Double.class);
    }

    /**
     * 是否为 String 类型
     *
     * @param type the type
     * @return the boolean
     */
    public static boolean isString(Class<?> type) {
        return type.equals(String.class);
    }
}
