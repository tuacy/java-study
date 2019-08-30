package com.tuacy.tableshard.tableextend.tableshard;

import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * @name: TableShardParam
 * @author: tuacy.
 * @date: 2019/8/30.
 * @version: 1.0
 * @Description:
 */
@Documented
@Inherited
@Target({ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface TableShardParam {

    @AliasFor("dependFieldName")
    String value() default "";

    /**
     * dependFieldName取到我们需要的获取表名的依据
     */
    @AliasFor("value")
    String dependFieldName() default "";

}
