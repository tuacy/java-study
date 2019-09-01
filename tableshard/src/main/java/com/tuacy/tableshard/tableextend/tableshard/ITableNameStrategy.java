package com.tuacy.tableshard.tableextend.tableshard;

/**
 * @name: ITableNameStrategy
 * @author: tuacy.
 * @date: 2019/8/13.
 * @version: 1.0
 * @Description: 分表对应的策略
 */
public interface ITableNameStrategy {

    /**
     * 表名字
     *
     * @param oldTableName     表基本名字
     * @param dependFieldValue 根据该字段确定表名
     * @return 表名
     */
    String tableName(String oldTableName, String dependFieldValue);

}
