package com.tuacy.tableshard.mapper;

import java.util.List;

/**
 * 用于提供一些公共的方法
 */
public interface BaseMapper {

    /**
     * 查询数据库里面所有的表
     */
    List<String> selectAllTableName();

}
