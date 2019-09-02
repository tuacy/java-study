package com.tuacy.tableshard.tableextend.tableshard;

import java.lang.annotation.*;

/**
 * @name: TableCreate
 * @author: tuacy.
 * @date: 2019/8/29.
 * @version: 1.0
 * @Description: TableCreate注解用于告诉我们怎么找到建表语句(如果表不存在的情况下, 我们程序里面自己去建表)
 * <p>
 * tableName -- 基础表名
 * autoCreateTableMapperClass -- mapper class对应的名字
 * autoCreateTableMapperMethodName -- mapper class 里面对应的方法
 * <p>
 * 最终我们会去mapper class里面找到对应的对应的方法,最终拿到建表语句
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface TableCreate {

    /**
     * table的基础表名
     */
    String tableName();

    /**
     * Mapper类，不能为空
     */
    Class<?> autoCreateTableMapperClass();

    /**
     * Mapper文件里面的函数名字（创建表对应的函数）
     */
    String autoCreateTableMapperMethodName();
}
