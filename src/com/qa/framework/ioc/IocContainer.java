package com.qa.framework.ioc;

import com.qa.framework.PageBase;
import com.qa.framework.ServiceBase;
import com.qa.framework.ioc.annotation.Page;
import com.qa.framework.ioc.annotation.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kcgw001 on 2016/4/13.
 */
public class IocContainer {

    private static final Map<Class<?>, Object> container = new HashMap<Class<?>, Object>();

    static {
        try {
            // 获取应用包路径下所有的类
            List<Class<?>> classList = ClassFinder.getClassList();
            for (Class<?> cls : classList) {
                if (cls.isAnnotationPresent(Service.class)
                        || cls.isAnnotationPresent(Page.class)
                        || (ServiceBase.class.isAssignableFrom(cls) && !cls.equals(ServiceBase.class))
                        || (PageBase.class.isAssignableFrom(cls) && !cls.equals(PageBase.class))) {
                    Object instance = cls.newInstance();
                    container.put(cls, instance);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("初始化类出错！" + e.getCause());
        }
    }

    public static Map<Class<?>, Object> getIocMap() {
        return container;
    }


    @SuppressWarnings("unchecked")
    public static <T> T getIocObject(Class<T> cls) {
        if (!container.containsKey(cls)) {
            return null;
        }
        return (T) container.get(cls);
    }

    public static void setIocObject(Class<?> cls, Object obj) {
        container.put(cls, obj);
    }
}
