package com.tuacy.tableshard.tableextend.multisource;

import java.lang.annotation.*;

/**
 * @name: DataSourceAnnotation
 * @author: tuacy.
 * @date: 2019/6/24.
 * @version: 1.0
 * @Description: 多数据源对应的注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DataSourceAnnotation {

    /**
     * 数据源类型
     * @return 数据源类型
     */
    EDataSourceType sourceType();
}
