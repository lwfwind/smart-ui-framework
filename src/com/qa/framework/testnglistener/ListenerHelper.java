package com.qa.framework.testnglistener;

import com.qa.framework.Inject.annotation.Page;
import com.qa.framework.SpringContext;
import com.qa.framework.cache.DriverCache;
import com.qa.framework.common.Driver;
import com.qa.framework.pagefactory.PageFactory;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.concurrent.TimeUnit;

public class ListenerHelper {
    /**
     * The constant logger.
     */
    protected final static Logger logger = Logger.getLogger(ListenerHelper.class);


    public static Object findImplementClass(Class clazz) {
        ApplicationContext applicationContext = SpringContext.getApplicationContext();
        if (applicationContext != null) {
            String[] beanNames = applicationContext.getBeanDefinitionNames();
            for (String beanName : beanNames) {
                Object bean = SpringContext.getBean(beanName);
                if (bean.getClass().getName().equals(clazz.getName())) {
                    return bean;
                } else {
                    Class<?>[] beanImplementClasses = bean.getClass().getInterfaces();
                    for (Class<?> beanImplementClass : beanImplementClasses) {
                        if (beanImplementClass.getName().equals(clazz.getName())) {
                            return bean;
                        }
                    }
                }
            }
        }
        return null;
    }

}
