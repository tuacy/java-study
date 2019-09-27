package com.tuacy.study.springboot.aware.ImportAware;

import java.lang.annotation.*;

/**
 * @name: ChangeAttribute
 * @author: tuacy.
 * @date: 2019/9/27.
 * @version: 1.0
 * @Description:
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ChangeAttribute {
    String value() default "";
}
