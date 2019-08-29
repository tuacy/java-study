package com.tuacy.tableshard.tableextend.tableshard;

import java.lang.annotation.*;

/**
 * @name: TablePrepareHandler
 * @author: tuacy.
 * @date: 2019/8/29.
 * @version: 1.0
 * @Description:
 */
@Documented
@Inherited
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface TablePrepareHandler {

    /**
     * 启用自动建表
     */
    boolean enableAutoCreateTable() default false;

    /**
     * 启用分表
     */
    boolean enableTableShard() default false;

    /**
     * 指定表，如果设置该值，则只会处理指定的表，没有则会处理sql中的所有表
     */
    String[] appointTable() default {};

    /**
     * 从参数的那个对象获取到我们需要的表名依据，如果没有传递则表的名字直接是tableBaseName对应的名字
     * 如果此处为空，需要再去判断是否使用了TableShardParam这个注解
     */
    Class<?> dependClassType() default void.class;

    /**
     * dependClassType，dependFieldName取到我们需要的获取表名的依据
     * 如果此处为空，需要再去判断是否使用了TableShardParam这个注解
     */
    String dependFieldName() default "";

    /**
     * 表名策略，通过某种规则得到表名
     * 如果此处为空，需要再去判断是否使用了TableShardParam这个注解
     */
    Class<? extends ITableNameStrategy> strategy() default TableNameStrategyVoid.class;
}
