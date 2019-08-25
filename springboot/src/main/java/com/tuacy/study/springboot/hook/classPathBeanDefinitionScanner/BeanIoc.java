package com.tuacy.study.springboot.hook.classPathBeanDefinitionScanner;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 添加了这个注解的类我们才会放到IOC容器里面去
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AutoIocScanRegistrar.class)
public @interface BeanIoc {
}
