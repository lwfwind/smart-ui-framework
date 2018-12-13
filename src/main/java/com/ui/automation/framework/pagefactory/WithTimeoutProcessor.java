package com.ui.automation.framework.pagefactory;

import java.lang.reflect.Field;

/**
 * Processes the iface type into a useful class reference for wrapping WebElements.
 */
public final class WithTimeoutProcessor {
    private WithTimeoutProcessor() {
    }

    /**
     * Gets time out of find.
     *
     * @param field the field
     * @param type  the type
     * @return the time out of find
     */
    public static int getTimeOutOfFind(Field field, int type) {
        if (field.isAnnotationPresent(WithTimeout.class)) {
            WithTimeout annotation = field.getAnnotation(WithTimeout.class);
            return annotation.value();
        }
        if (type == 1) {
            return 10000;
        } else {
            return 20000;
        }
    }

}
