package com.tuacy.study.springboot.hook.importBeanDefinitionRegistrar;

import com.tuacy.study.springboot.hook.importBeanDefinitionRegistrar.RunStartScannerRegister;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @name: RunStartScan
 * @author: tuacy.
 * @date: 2019/8/16.
 * @version: 1.0
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RunStartScannerRegister.class)
public @interface RunStartScan {
    String[] basePackages() default "";
}
