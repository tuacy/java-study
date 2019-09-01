package com.tuacy.tableshard.dao.impl;

import com.google.common.collect.Lists;
import com.tuacy.tableshard.dao.BaseDao;
import com.tuacy.tableshard.dao.IAccHourDao;
import com.tuacy.tableshard.entity.model.AccHour;
import com.tuacy.tableshard.lang.TwoTuple;
import com.tuacy.tableshard.mapper.AccHourMapper;
import com.tuacy.tableshard.tableextend.multisource.DataSourceAnnotation;
import com.tuacy.tableshard.tableextend.multisource.EDataSourceType;
import com.tuacy.tableshard.tableextend.tableshard.ITableNameStrategy;
import com.tuacy.tableshard.tableextend.tableshard.namestrategy.SuffixYYYYMMDDHHNameStrategy;
import com.tuacy.tableshard.utils.DbDataTimeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;


@Repository
public class AccHourDao extends BaseDao implements IAccHourDao {

    /**
     * 基础表名
     */
    private static final String BASE_TABLE_NAME = "StatisAccHour";
    /**
     * 分表策略
     */
    private static final ITableNameStrategy STRATEGY = new SuffixYYYYMMDDHHNameStrategy();

    private AccHourMapper accHourMapper;

    @Autowired
    public void setAccHourMapper(AccHourMapper accHourMapper) {
        this.accHourMapper = accHourMapper;
    }

    /**
     * DataSourceAnnotation 用于指定数据源,放到统计数据库里面
     */
    @Override
    @DataSourceAnnotation(sourceType = EDataSourceType.STATIS)
    @Transactional(rollbackFor = Exception.class)
    public int insertItem(AccHour item) {
        return accHourMapper.insertItem(item);
    }

    @Override
    @DataSourceAnnotation(sourceType = EDataSourceType.STATIS)
    @Transactional(rollbackFor = Exception.class)
    public int insertList(List<AccHour> list) {
        if (list == null || list.isEmpty()) {
            return 0;
        }
        // 首先,我们不能保证list所有的数据都是一张表的,所以我们先得对数据分类,按表来分类
        Map<String, List<AccHour>> groupingByTable = list.stream().collect(Collectors.groupingBy(
                item -> STRATEGY.tableName(BASE_TABLE_NAME, item.getRecTime().toString()),
                (Supplier<Map<String, List<AccHour>>>) HashMap::new,
                Collectors.toList()));
        // 遍历存储(上面的代码我们已经保存了每个Map.)
        int sucessCount = 0;
        for (List<AccHour> mapValueItem : groupingByTable.values()) {
            sucessCount += accHourMapper.insertList(mapValueItem);
        }
        return sucessCount;
    }

    @Override
    @DataSourceAnnotation(sourceType = EDataSourceType.STATIS)
    @Transactional(rollbackFor = Exception.class)
    public AccHour selectItem(Long recvTime, Long ptId) {
        return accHourMapper.selectItem(recvTime, ptId);
    }

    /**
     * 查询指定时间范围的数据
     * 针对time做一个简单说明,
     * 比如当前时间是 2019年08月31日00时31分46秒141微妙
     * 则我们在数据库里面存20190831003146141
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 所有查询到的记录
     */
    @Override
    @DataSourceAnnotation(sourceType = EDataSourceType.STATIS)
    @Transactional(rollbackFor = Exception.class)
    public List<AccHour> selectList(Long startTime, Long endTime) {
        // long类型是20190831003146141的形式转换为2019年08月31日00时31分46秒141微妙对应的LocalDateTime
        LocalDateTime startTimeDate = DbDataTimeUtils.long2DateTime(startTime);
        LocalDateTime endTimeDate = DbDataTimeUtils.long2DateTime(endTime);
        if (startTimeDate.isAfter(endTimeDate)) {
            return null;
        }
        // 数据库里面所有的表
        List<String> allTableName = allTableName();
        if (allTableName == null || allTableName.isEmpty()) {
            return null;
        }
        // 全部转换成小写
        allTableName = allTableName.stream().map(String::toLowerCase).collect(Collectors.toList());
        List<TwoTuple<Long, Long>> singleTableConditionList = Lists.newArrayList();
        // 我们已经确定了当前是按小时分表的,表名类似于 StatisAccHour2019083122 的形式,先确定指定的时间范围里面有多少张表
        while (startTimeDate.isBefore(endTimeDate) || startTimeDate.equals(endTimeDate)) {
            String tableName = STRATEGY.tableName(BASE_TABLE_NAME, String.valueOf(DbDataTimeUtils.dateTime2Long(startTimeDate)));
            if (allTableName.contains(tableName.toLowerCase())) {
                // 有这个表存在
                Long singleTableStartTime = DbDataTimeUtils.dateTime2Long(startTimeDate);
                if (singleTableStartTime < startTime) {
                    singleTableStartTime = startTime;
                }
                singleTableConditionList.add(new TwoTuple<>(singleTableStartTime, endTime));
            }
            startTimeDate = startTimeDate.plusHours(1);
        }
        if (singleTableConditionList.isEmpty()) {
            return null;
        }
        List<AccHour> retList = Lists.newArrayList();
        for (TwoTuple<Long, Long> item : singleTableConditionList) {
            retList.addAll(accHourMapper.selectLIst(item.getFirst(), item.getSecond()));
        }

        return retList;
    }
}
