package com.qa.framework.ioc;

import com.qa.framework.config.PropConfig;
import com.qa.framework.config.Value;
import com.qa.framework.ioc.annotation.Autowired;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;

import static com.qa.framework.ioc.IocHelper.findImplementClass;

public class AutoInjectHelper {
    /**
     * The constant logger.
     */
    protected final static Logger logger = Logger.getLogger(AutoInjectHelper.class);

    /**
     * Init fields.
     *
     * @param obj the obj
     */
    public static void initFields(Object obj) {
        Class<?> clazz = obj.getClass();
        boolean isAbs = Modifier.isAbstract(clazz.getModifiers());
        while (clazz != Object.class && !isAbs) {
            proxyFields(obj, clazz);
            clazz = clazz.getSuperclass();
            isAbs = Modifier.isAbstract(clazz.getModifiers());
        }
    }

    private static void proxyFields(Object obj, Class<?> clazz) {
        do {
            Field[] fields = clazz.getDeclaredFields();
            fillForFields(obj, fields);

            if (clazz.getSuperclass() == null) {
                return;
            }
            clazz = clazz.getSuperclass();

        } while (true);
    }

    private static void fillForFields(Object obj, Field[] fields) {
        for (Field field : fields) {
            if (field.getAnnotation(Autowired.class) != null) {
                Object proxy = IocContainer.getIocObject(field.getType());
                if (proxy == null) {
                    logger.debug(field.getName() + " is not existed in IOC Container");
                    Class<?> clazz = field.getType();
                    Class<?> implementClass = null;
                    if (Modifier.isAbstract(clazz.getModifiers()) || Modifier.isInterface(clazz.getModifiers())) {
                        implementClass = findImplementClass(clazz);

                    } else {
                        implementClass = clazz;
                    }

                    try {
                        if (implementClass != null) {
                            proxy = implementClass.newInstance();
                        }
                    } catch (InstantiationException | IllegalAccessException e) {
                        logger.error(e.toString(), e);
                    }
                } else {
                    logger.debug(field.getName() + " is existed in IOC Container");
                }
                try {
                    field.setAccessible(true);
                    field.set(obj, proxy);
                } catch (IllegalAccessException e) {
                    logger.error(e.toString(), e);
                }
            }
            else if(field.getAnnotation(Value.class) != null){
                Value value = field.getAnnotation(Value.class);
                String fieldKey = value.value();
                Properties props = PropConfig.getProps();
                if (props.getProperty(fieldKey) != null) {
                    String fieldValue = props.getProperty(fieldKey);
                    field.setAccessible(true);
                    setValue(obj,field,fieldValue);
                }
            }
        }
    }

    private static void setValue(Object obj, Field field, String value) {
        Object fieldType = field.getType();
        try {
            if (String.class.equals(fieldType)) {
                field.set(obj, value);
            } else if (byte.class.equals(fieldType)) {
                field.set(obj, Byte.parseByte(value));
            } else if (Byte.class.equals(fieldType)) {
                field.set(obj, Byte.valueOf(value));
            } else if (boolean.class.equals(fieldType)) {
                field.set(obj, Boolean.parseBoolean(value));
            } else if (Boolean.class.equals(fieldType)) {
                field.set(obj, Boolean.valueOf(value));
            } else if (short.class.equals(fieldType)) {
                field.set(obj, Short.parseShort(value));
            } else if (Short.class.equals(fieldType)) {
                field.set(obj, Short.valueOf(value));
            } else if (int.class.equals(fieldType)) {
                field.set(obj, Integer.parseInt(value));
            } else if (Integer.class.equals(fieldType)) {
                field.set(obj, Integer.valueOf(value));
            } else if (long.class.equals(fieldType)) {
                field.set(obj, Long.parseLong(value));
            } else if (Long.class.equals(fieldType)) {
                field.set(obj, Long.valueOf(value));
            } else if (float.class.equals(fieldType)) {
                field.set(obj, Float.parseFloat(value));
            } else if (Float.class.equals(fieldType)) {
                field.set(obj, Float.valueOf(value));
            } else if (double.class.equals(fieldType)) {
                field.set(obj, Double.parseDouble(value));
            } else if (Double.class.equals(fieldType)) {
                field.set(obj, Double.valueOf(value));
            }
        } catch (IllegalAccessException e) {
            logger.error(e.getMessage(), e);
        }
    }
}
