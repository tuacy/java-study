package com.tuacy.tableshard.mapper;

import com.tuacy.tableshard.entity.model.AccHour;
import com.tuacy.tableshard.tableextend.tableshard.TablePrepare;
import com.tuacy.tableshard.tableextend.tableshard.TableShardParam;
import com.tuacy.tableshard.tableextend.tableshard.namestrategy.SuffixYYYYMMDDHHNameStrategy;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * AccHour 每个小时一张表(多数据源,我们有三个数据源,我们假设该表放在statis数据源下面)
 */
public interface AccHourMapper {

    /**
     * 往数据库里面插入一条记录
     */
    @TablePrepare(enableTableShard = true, strategy = SuffixYYYYMMDDHHNameStrategy.class)
    int insertItem(@TableShardParam(dependFieldName = "recTime") @Param("item") AccHour item);

    /**
     * 往数据库里面插入多条
     */
    @TablePrepare(enableTableShard = true, strategy = SuffixYYYYMMDDHHNameStrategy.class)
    int insertList(@TableShardParam(dependFieldName = "recTime") @Param("list") List<AccHour> list);

    /**
     * 往数据库里面插入多条
     */
    @TablePrepare(enableTableShard = true, strategy = SuffixYYYYMMDDHHNameStrategy.class)
    AccHour selectItem(@TableShardParam(dependFieldName = "recTime") @Param("recvTime") Long recvTime, @Param("pkId") Long pkId);

    /**
     * 查询指定时间范围内的列表
     *
     * @param startTIme 开始时间
     * @param endTime   解释时间
     */
    @TablePrepare(enableTableShard = true, strategy = SuffixYYYYMMDDHHNameStrategy.class)
    List<AccHour> selectLIst(@TableShardParam() @Param("startTime") Long startTIme, @Param("endTime") Long endTime);


}
