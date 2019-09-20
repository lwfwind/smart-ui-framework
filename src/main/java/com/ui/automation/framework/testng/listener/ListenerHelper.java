package com.ui.automation.framework.testng.listener;

import com.ui.automation.framework.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;

@Slf4j
public class ListenerHelper {

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
