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
@Conditional(OnSystemCondition.class)
public @interface ConditionalOnSystem {

    /**
     * 指定系统
     */
    SystemType type() default SystemType.WINDOWS;

    /**
     * 系统类型
     */
    enum SystemType {

        /**
         * windows系统
         */
        WINDOWS,

        /**
         * linux系统
         */
        LINUX,

        /**
         * mac系统
         */
        MAC

    }
}
