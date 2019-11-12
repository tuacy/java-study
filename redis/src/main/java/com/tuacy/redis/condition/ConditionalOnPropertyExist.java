package com.tuacy.redis.condition;

import org.springframework.context.annotation.Conditional;

import java.lang.annotation.*;

/**
 * @name: ConditionalOnPropertyExist
 * @author: tuacy.
 * @date: 2019/11/12.
 * @version: 1.0
 * @Description:
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD})
@Documented
@Conditional(OnPropertyExistCondition.class)
public @interface ConditionalOnPropertyExist {

    /**
     * 配置文件里面对应的key
     */
    String name() default "";

    /**
     * 是否有配置的时候判断通过
     */
    boolean exist() default true;

}
