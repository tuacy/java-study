package com.tuacy.study.springboot.hook.importBeanDefinitionRegistrar.customercomponent;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @name: RunStart
 * @author: tuacy.
 * @date: 2019/8/16.
 * @version: 1.0
 * @Description: 添加这个注解的类在程序刚运行的时候，自动调用指定的函数
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(CustomerComponentScannerRegister.class)
public @interface CustomerComponentScan {
    String[] basePackages() default "";
}
