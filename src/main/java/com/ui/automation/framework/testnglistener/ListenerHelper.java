package com.ui.automation.framework.testnglistener;

import com.ui.automation.framework.SpringContext;
import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;

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
