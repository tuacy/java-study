package com.tuacy.tableshard.tableextend.tableshard.namestrategy;


import com.tuacy.tableshard.tableextend.tableshard.ITableNameStrategy;

/**
 * 分表方案 按照年月分表
 */
public class SuffixYYYYMMDDNameStrategy implements ITableNameStrategy {

    private static final int SUFFIX_LENGTH = 8; // yyyymmdd

    @Override
    public String tableName(String oldTableName, String dependFieldValue) {
        return oldTableName + dependFieldValue.substring(0, SUFFIX_LENGTH);
    }
}
