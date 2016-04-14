package com.qa.framework.pagefactory;

import java.lang.reflect.Field;

/**
 * Processes the iface type into a useful class reference for wrapping WebElements.
 */
public final class TimeOutOfFindProcessor {
    private TimeOutOfFindProcessor() {
    }

    /**
     * Gets time out of find.
     *
     * @param field the field
     * @return the time out of find
     */
    public static int getTimeOutOfFind(Field field) {
        if (field.isAnnotationPresent(TimeOutOfFind.class)) {
            TimeOutOfFind annotation = field.getAnnotation(TimeOutOfFind.class);
            return annotation.value();
        }
        return 10000;
    }

}
