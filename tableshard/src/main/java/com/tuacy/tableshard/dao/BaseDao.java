package com.tuacy.tableshard.dao;

import com.tuacy.tableshard.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public abstract class BaseDao {

    private BaseMapper baseMapper;

    @Autowired
    public void setBaseMapper(BaseMapper baseMapper) {
        this.baseMapper = baseMapper;
    }

    /**
     * 数据库里面所有的表
     */
    protected List<String> allTableName() {
        return baseMapper.selectAllTableName();
    }
}
