package com.qa.framework.ioc.impl.support;

/**
 * 用于获取子类的模板类
 *
 */
public abstract class SupperClassTemplate extends ClassTemplate {

    protected final Class<?> superClass;

    protected SupperClassTemplate(String packageName, Class<?> superClass) {
        super(packageName);
        this.superClass = superClass;
    }
}
