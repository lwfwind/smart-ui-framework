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
     * @param type  the type
     * @return the time out of find
     */
    public static int getTimeOutOfFind(Field field, int type) {
        if (field.isAnnotationPresent(TimeOutOfFind.class)) {
            TimeOutOfFind annotation = field.getAnnotation(TimeOutOfFind.class);
            return annotation.value();
        }
        if (type == 1) {
            return 10000;
        } else {
            return 20000;
        }
    }

}
