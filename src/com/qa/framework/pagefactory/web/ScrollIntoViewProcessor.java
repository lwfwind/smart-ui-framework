package com.qa.framework.pagefactory.web;

import java.lang.reflect.Field;

/**
 * Processes the iface type into a useful class reference for wrapping WebElements.
 */
public final class ScrollIntoViewProcessor {
    private ScrollIntoViewProcessor() {
    }

    public static Boolean getScrollIntoView(Field field) {
        if (field.isAnnotationPresent(ScrollIntoView.class)) {
            ScrollIntoView annotation = field.getAnnotation(ScrollIntoView.class);
            return annotation.value();
        }
        return false;
    }

}
