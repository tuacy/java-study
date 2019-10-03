package com.tuacy.study.springboot.aware.ImportAware;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * 注意这个类上的两个注解的使用
 * 1. @Import(BeanImportAware.class)，BeanImportAware类实现了ImportAware接口
 * 2. @ChangeAttribute是我们自定义的一个注解，用来带参数的。会在BeanImportAware类里面去获取这个主句
 */
@Configuration
@Import(BeanImportAware.class)
@ChangeAttribute(value = "tuacy")
public class ImportAwareConfig {
}
