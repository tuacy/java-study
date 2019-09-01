package com.tuacy.tableshard.tableextend.multisource;

/**
 * @name: EDataSourceType
 * @author: tuacy.
 * @date: 2019/6/24.
 * @version: 1.0
 * @Description: 假设我们有三个数据源(三个数据库), 我们直接用一个枚举来表示, 大家可以根据自己的需求做不同的定义
 */
public enum EDataSourceType {

    /**
     * 【基础】数据库数据源名称
     */
    BASIC,

    /**
     * 【历史】数据库数据源名称
     */
    HISTORY,

    /**
     * 【统计】数据库数据源名称
     */
    STATIS
}
