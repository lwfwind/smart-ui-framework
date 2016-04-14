package com.qa.framework.ioc;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * 类扫描器
 */
public interface ClassScanner {

    /**
     * 获取指定包名中的所有类
     *
     * @param packageName the package name
     * @return the class list
     */
    List<Class<?>> getClassList(String packageName);

    /**
     * 获取指定包名中指定注解的相关类
     *
     * @param packageName     the package name
     * @param annotationClass the annotation class
     * @return the class list by annotation
     */
    List<Class<?>> getClassListByAnnotation(String packageName, Class<? extends Annotation> annotationClass);

    /**
     * 获取指定包名中指定父类或接口的相关类
     *
     * @param packageName the package name
     * @param superClass  the super class
     * @return the class list by super
     */
    List<Class<?>> getClassListBySuper(String packageName, Class<?> superClass);
}
