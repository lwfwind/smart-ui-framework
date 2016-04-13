package com.qa.framework;


import com.qa.framework.ioc.IocContainer;
import com.qa.framework.ioc.IocHelper;
import com.qa.framework.library.base.ClassHelper;

/**
 * 加载相应的 Helper 类
 */
public final class HelperLoader {

    public static void init() {
        // 定义需要加载的 Helper 类
        Class<?>[] classList = {
                IocContainer.class,
                IocHelper.class,
        };
        // 按照顺序加载类
        for (Class<?> cls : classList) {
            ClassHelper.loadClass(cls.getName());
        }
    }
}
