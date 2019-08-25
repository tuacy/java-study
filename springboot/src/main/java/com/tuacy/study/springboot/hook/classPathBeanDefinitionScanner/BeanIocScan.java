package com.tuacy.study.springboot.hook.classPathBeanDefinitionScanner;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 定义搜索路径
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(AutoIocScanRegistrar.class)
public @interface BeanIocScan {
    String[] basePackages() default "";
}
