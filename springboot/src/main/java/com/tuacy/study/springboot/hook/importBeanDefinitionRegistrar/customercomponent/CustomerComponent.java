package com.tuacy.study.springboot.hook.importBeanDefinitionRegistrar.customercomponent;

import java.lang.annotation.*;

/**
 * @name: RunStart
 * @author: tuacy.
 * @date: 2019/8/16.
 * @version: 1.0
 * @Description: 和@Component的功能一样
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface CustomerComponent {

}
