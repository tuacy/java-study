package com.tuacy.tableshard.tableextend.tableshard;

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

    EParamType paramType() default EParamType.PRIMITIVE;

    /**
     * dependClassType，dependFieldName取到我们需要的获取表名的依据
     */
    String dependFieldName() default "";

    /**
     * 表名策略，通过某种规则得到表名
     */
    Class<? extends ITableNameStrategy> strategy() default TableNameStrategyVoid.class;


    enum EParamType {
        PRIMITIVE, OBJECT
    }

}
