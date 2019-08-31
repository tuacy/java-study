package com.tuacy.tableshard.dao;

import com.tuacy.tableshard.entity.model.AccHour;

import java.util.List;

public interface IAccHourDao {

    /**
     * 插入单条记录
     */
    int insertItem(AccHour item);

    /**
     * 插入多条记录,这里要特别注意,参数list里面的数据不一定都在一个表内,所以要特别处理
     */
    int insertList(List<AccHour> list);

    /**
     * 查询单条记录
     */
    AccHour selectItem(Long recvTime, Long ptId);

    /**
     * 我们查询指定时间范围内所有的记录
     */
    List<AccHour> selectList(Long startTime, Long endTime);

}
