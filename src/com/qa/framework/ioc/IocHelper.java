package com.qa.framework.ioc;

import com.qa.framework.ioc.annotation.AutoInject;
import com.qa.framework.ioc.annotation.Impl;
import com.qa.framework.ioc.annotation.Service;
import com.qa.framework.library.base.ArrayUtil;
import com.qa.framework.library.base.CollectionHelper;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 初始化 IOC 容器
 *
 */
public class IocHelper {

    private static final Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();

    static {

        try {
            // 获取应用包路径下所有的类
            List<Class<?>> classList = ClassFinder.getClassList();
            for (Class<?> cls : classList) {
                // 处理带有 Bean/Service/Action/Aspect 注解的类
                if (cls.isAnnotationPresent(Service.class)) {
                    // 创建 Bean 实例
                    Object beanInstance = cls.newInstance();
                    // 将 Bean 实例放入 Bean Map 中（键为 Bean 类，值为 Bean 实例）
                    map.put(cls, beanInstance);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("初始化Service类出错！", e);
        }

        try {
            for (Map.Entry<Class<?>, Object> beanEntry : map.entrySet()) {
                // 获取 Bean 类与 Bean 实例
                Class<?> beanClass = beanEntry.getKey();
                Object beanInstance = beanEntry.getValue();
                // 获取 Bean 类中所有的字段（不包括父类中的方法）
                Field[] beanFields = beanClass.getDeclaredFields();
                if (ArrayUtil.isNotEmpty(beanFields)) {
                    // 遍历所有的 Bean 字段
                    for (Field beanField : beanFields) {
                        // 判断当前 Bean 字段是否带有 Inject 注解
                        if (beanField.isAnnotationPresent(AutoInject.class)) {
                            // 获取 Bean 字段对应的接口
                            Class<?> interfaceClass = beanField.getType();
                            // 获取 Bean 字段对应的实现类
                            Class<?> implementClass = findImplementClass(interfaceClass);
                            // 若存在实现类，则执行以下代码
                            if (implementClass != null) {
                                // 从 Bean Map 中获取该实现类对应的实现类实例
                                Object implementInstance = map.get(implementClass);
                                // 设置该 Bean 字段的值
                                if (implementInstance != null) {
                                    beanField.setAccessible(true); // 将字段设置为 public
                                    beanField.set(beanInstance, implementInstance); // 设置字段初始值
                                } else {
                                    throw new RuntimeException("依赖注入失败！类名：" + beanClass.getSimpleName() + "，字段名：" + interfaceClass.getSimpleName());
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
