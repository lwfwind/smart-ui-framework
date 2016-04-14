package com.qa.framework.ioc;

import com.qa.framework.ioc.annotation.AutoInject;
import com.qa.framework.ioc.annotation.Impl;
import com.qa.framework.library.base.ArrayUtil;
import com.qa.framework.library.base.CollectionHelper;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

/**
 * 初始化 IOC 容器
 */
public class IocHelper {

    static {
        try {
            Map<Class<?>, Object> iocContainer = IocContainer.getIocMap();
            for (Map.Entry<Class<?>, Object> iocObjEntry : iocContainer.entrySet()) {
                Class<?> iocClass = iocObjEntry.getKey();
                Object iocInstance = iocObjEntry.getValue();
                Field[] fields = iocClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(fields)) {
                    for (Field field : fields) {
                        if (field.isAnnotationPresent(AutoInject.class)) {
                            Class<?> interfaceClass = field.getType();
                            Class<?> implementClass = findImplementClass(interfaceClass);
                            if (implementClass != null) {
                                Object implementInstance = iocContainer.get(implementClass);
                                if (implementInstance != null) {
                                    field.setAccessible(true); // 将字段设置为 public
                                    field.set(iocInstance, implementInstance); // 设置字段初始值
                                } else {
                                    throw new RuntimeException("依赖注入失败！类名：" + iocClass.getSimpleName() + "，字段名：" + interfaceClass.getSimpleName());
                                }
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("初始化 IocHelper 出错！", e);
        }
    }

    /**
     * 查找实现类
     */
    public static Class<?> findImplementClass(Class<?> interfaceClass) {
        Class<?> implementClass = interfaceClass;
        // 判断接口上是否标注了 Impl 注解
        if (interfaceClass.isAnnotationPresent(Impl.class)) {
            // 获取强制指定的实现类
            implementClass = interfaceClass.getAnnotation(Impl.class).value();
        } else {
            // 获取该接口所有的实现类
            List<Class<?>> implementClassList = ClassFinder.getClassListBySuper(interfaceClass);
            if (CollectionHelper.isNotEmpty(implementClassList)) {
                // 获取第一个实现类
                implementClass = implementClassList.get(0);
            }
        }
        // 返回实现类对象
        return implementClass;
    }
}
