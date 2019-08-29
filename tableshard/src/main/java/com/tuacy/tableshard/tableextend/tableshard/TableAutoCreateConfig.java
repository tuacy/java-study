package com.tuacy.tableshard.tableextend.tableshard;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @name: TableAutoCreateConfig
 * @author: tuacy.
 * @date: 2019/8/29.
 * @version: 1.0
 * @Description: 自动建表相关的一些配置
 */
@Data
@Accessors(chain = true)
public class TableAutoCreateConfig {

    /**
     * 表名
     */
    private String tableName;

    /**
     * 自动建表Mapper类
     */
    private Class<?> autoCreateTableMapperClass;

    /**
     * 自动建表Mapper中的方法
     */
    private String autoCreateTableMapperMethodName;

}
