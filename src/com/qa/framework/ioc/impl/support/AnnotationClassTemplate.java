package com.qa.framework.ioc.impl.support;

import java.lang.annotation.Annotation;

/**
 * 用于获取注解类的模板类
 */
public abstract class AnnotationClassTemplate extends ClassTemplate {

    /**
     * The Annotation class.
     */
    protected final Class<? extends Annotation> annotationClass;

    /**
     * Instantiates a new Annotation class template.
     *
     * @param packageName     the package name
     * @param annotationClass the annotation class
     */
    protected AnnotationClassTemplate(String packageName, Class<? extends Annotation> annotationClass) {
        super(packageName);
        this.annotationClass = annotationClass;
    }
}
