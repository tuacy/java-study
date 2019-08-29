package com.tuacy.tableshard.tableextend.tableshard;

import java.lang.annotation.*;

/**
 * @name: ATableAutoCreate
 * @author: tuacy.
 * @date: 2019/8/29.
 * @version: 1.0
 * @Description:
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ATableAutoCreate {

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
     * 支持两种情况：1.函数没有参数的时候，mapper.xml里面直接指定了表的名字
     *            2.函数只有一个参数--表名
     */
    String autoCreateTableMapperMethodName();
}
