package com.qa.framework.library.reflect;

import java.lang.reflect.Field;

/**
 * Processes the iface type into a useful class reference for wrapping WebElements.
 */
public final class AutoInjectProcessor {
    private AutoInjectProcessor() {
    }

    /**
     * Gets auto inject.
     *
     * @param field the field
     * @return the auto inject
     */
    public static Boolean getAutoInject(Field field) {
        if (field.isAnnotationPresent(AutoInject.class)) {
            return true;
        }
        return false;
    }

}
