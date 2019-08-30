package com.tuacy.tableshard.tableextend.tableshard.namestrategy;


import com.tuacy.tableshard.tableextend.tableshard.ITableNameStrategy;

/**
 * 分表方案 按照年月日时分表
 */
public class SuffixYYYYMMDDHHNameStrategy implements ITableNameStrategy {

    private static final int SUFFIX_LENGTH = 10; // yyyymmddhh

    @Override
    public String tableName(String baseTableName, String dependFieldValue) {
        return baseTableName + dependFieldValue.substring(0, SUFFIX_LENGTH);
    }
}
