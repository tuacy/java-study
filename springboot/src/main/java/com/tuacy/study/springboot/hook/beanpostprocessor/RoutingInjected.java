package com.tuacy.study.springboot.hook.beanpostprocessor;

import java.lang.annotation.*;

/**
 * @name: RoutingInjected
 * @author: tuacy.
 * @date: 2019/8/23.
 * @version: 1.0
 * @Description:
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RoutingInjected {
}
