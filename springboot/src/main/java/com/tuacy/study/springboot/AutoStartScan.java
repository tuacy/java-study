package com.tuacy.study.springboot;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @name: AutoStartScan
 * @author: tuacy.
 * @date: 2019/8/16.
 * @version: 1.0
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(AutoStartScannerRegister.class)
public @interface AutoStartScan {
    String[] basePackages() default "";
}
