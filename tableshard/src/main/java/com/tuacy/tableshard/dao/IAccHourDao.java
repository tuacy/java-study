package com.tuacy.tableshard.dao;

import com.tuacy.tableshard.entity.model.AccHour;

public interface IAccHourDao {

    /**
     * 插入单条记录
     */
    int insertItem(AccHour item);

    /**
     * 查询单条记录
     */
    AccHour selectItem(Long time, Long pkId);

}
