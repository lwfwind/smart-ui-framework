package com.qa.framework.ioc;



import com.qa.framework.ioc.annotation.Value;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Properties;

/**
 * Created by Administrator on 2016/11/10.
 */
public class ValueHelp {
    public static void initConfigFields(Object obj) {
            Properties props = new Properties();
            Class<?> clazz = obj.getClass();
            props = getProperties();
            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                String fieldKey;
                String fieldValue;
                if (field.isAnnotationPresent(Value.class)) {
                    Value value = field.getAnnotation(Value.class);
                    fieldKey = value.value();
                    if (!field.getName().equals("props") && props.getProperty(fieldKey) != null) {
                        fieldValue = props.getProperty(fieldKey);
                        field.setAccessible(true);
                        setValue(obj,field,fieldValue);
                            }
                    }
                }
            }

    private static void setValue(Object obj, Field method, String value) {
        Object fieldType=method.getType();
        try {
            if (String.class.equals(fieldType)) {
                method.set(obj, value.toString());
            } else if (byte.class.equals(fieldType)) {
                method.set(obj, Byte.parseByte(value.toString()));
            } else if (Byte.class.equals(fieldType)) {
                method.set(obj, Byte.valueOf(value.toString()));
            } else if (boolean.class.equals(fieldType)) {
                method.set(obj, Boolean.parseBoolean(value.toString()));
            } else if (Boolean.class.equals(fieldType)) {
                method.set(obj, Boolean.valueOf(value.toString()));
            } else if (short.class.equals(fieldType)) {
                method.set(obj, Short.parseShort(value.toString()));
            } else if (Short.class.equals(fieldType)) {
                method.set(obj, Short.valueOf(value.toString()));
            } else if (int.class.equals(fieldType)) {
                method.set(obj, Integer.parseInt(value.toString()));
            } else if (Integer.class.equals(fieldType)) {
                method.set(obj, Integer.valueOf(value.toString()));
            } else if (long.class.equals(fieldType)) {
                method.set(obj, Long.parseLong(value.toString()));
            } else if (Long.class.equals(fieldType)) {
                method.set(obj, Long.valueOf(value.toString()));
            } else if (float.class.equals(fieldType)) {
                method.set(obj, Float.parseFloat(value.toString()));
            } else if (Float.class.equals(fieldType)) {
                method.set(obj, Float.valueOf(value.toString()));
            } else if (double.class.equals(fieldType)) {
                method.set(obj, Double.parseDouble(value.toString()));
            } else if (Double.class.equals(fieldType)) {
                method.set(obj, Double.valueOf(value.toString()));
            }
        } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
    }


    public static Properties getProperties() {
        Properties props = new Properties();
        File file = new File(System.getProperty("user.dir") + File.separator + "config.properties");
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(file);
            props.load(fileReader);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return props;
    }
}
