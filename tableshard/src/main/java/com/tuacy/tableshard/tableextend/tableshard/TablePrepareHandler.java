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
     * 表名策略，通过某种规则得到表名
     * 如果此处为空，需要再去判断是否使用了TableShardParam这个注解
     */
    Class<? extends ITableNameStrategy> strategy() default TableNameStrategyVoid.class;
}
