package com.ui.automation.framework.pagefactory.web;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Sets the default implementing class for the annotated interface.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ScrollIntoView {
    /**
     * Value boolean.
     *
     * @return the boolean
     */
    boolean value() default false;
}
