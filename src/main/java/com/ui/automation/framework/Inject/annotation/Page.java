package com.ui.automation.framework.Inject.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 定义 Service 类
 *
 * @author huangyong
 * @since 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Page {
}
