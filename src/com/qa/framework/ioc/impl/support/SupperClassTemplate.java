package com.qa.framework.ioc.impl.support;

/**
 * 用于获取子类的模板类
 */
public abstract class SupperClassTemplate extends ClassTemplate {

    /**
     * The Super class.
     */
    protected final Class<?> superClass;

    /**
     * Instantiates a new Supper class template.
     *
     * @param packageName the package name
     * @param superClass  the super class
     */
    protected SupperClassTemplate(String packageName, Class<?> superClass) {
        super(packageName);
        this.superClass = superClass;
    }
}
