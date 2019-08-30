package com.tuacy.tableshard.mapper;

import com.tuacy.tableshard.entity.model.AccHour;
import com.tuacy.tableshard.tableextend.tableshard.TablePrepare;
import com.tuacy.tableshard.tableextend.tableshard.TableShardParam;
import com.tuacy.tableshard.tableextend.tableshard.namestrategy.SuffixYYYYMMDDHHNameStrategy;
import org.apache.ibatis.annotations.Param;

/**
 * AccHour 每个小时一张表
 */
public interface AccHourMapper {

    /**
     * 往数据库里面插入一条记录
     */
    @TablePrepare(enableTableShard = true, strategy = SuffixYYYYMMDDHHNameStrategy.class)
    int insertItem(@TableShardParam(dependFieldName = "recTime") @Param("item") AccHour item);

}
