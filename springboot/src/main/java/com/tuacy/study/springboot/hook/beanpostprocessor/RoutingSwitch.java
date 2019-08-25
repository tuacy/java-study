package com.tuacy.study.springboot.hook.beanpostprocessor;

import java.lang.annotation.*;

/**
 * @name: RoutingSwitch
 * @author: tuacy.
 * @date: 2019/8/23.
 * @version: 1.0
 * @Description:
 */
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoutingSwitch {

    /**
     * 在配置系统中开关的属性名称，应用系统将会实时读取配置系统中对应的开关的值来决定调用哪个版本
     */
    Class switchClass();

}
