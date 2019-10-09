package com.tuacy.tableshard.dao;

import com.tuacy.tableshard.mapper.BaseMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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

    /**
     * 1. 先按照表分类
     * 2. 在一段一段的处理
     *
     * @param list            数据源
     * @param tableShardBasis 分表依据
     * @param pieSize         分批次处理的个数，比如做数据库的插入操作防止一次性插入太多
     * @param action          动作(比如数据库的插入，数据库的更新)
     * @return 总共操作了多少条
     */
    protected <ITEM> int batchHandle(List<ITEM> list, Function<ITEM, String> tableShardBasis, int pieSize, Function<List<ITEM>, Integer> action) {

        if (list == null || list.isEmpty()) {
            return 0;
        }

        // 按照数据库分组，每个分组对应一个数据库
        Map<String, List<ITEM>> modelGroup = list.stream().collect(Collectors.groupingBy(
                tableShardBasis,
                (Supplier<Map<String, List<ITEM>>>) HashMap::new,
                Collectors.toList()));


        int actionNum = 0;
        for (Map.Entry<String, List<ITEM>> entry : modelGroup.entrySet()) {
            String mapKey = entry.getKey();
            List<ITEM> mapValue = entry.getValue();

            // 分批次处理，防止一次性处理太多
            int batchCount = mapValue.size() % pieSize == 0 ? mapValue.size() / pieSize : mapValue.size() / pieSize + 1;
            for (int batchIndex = 0; batchIndex < batchCount; batchIndex++) {
                int subStartIndex = batchIndex * pieSize;
                int subEndIndex = (batchIndex + 1) * pieSize;
                if (subEndIndex > mapValue.size()) {
                    subEndIndex = mapValue.size();
                }
                actionNum += action.apply(mapValue.subList(subStartIndex, subEndIndex));
            }
        }
        return actionNum;
    }

    /**
     * 1. 一段一段的处理
     *
     * @param list    数据源
     * @param pieSize 分批次处理的个数，比如做数据库的插入操作防止一次性插入太多
     * @param action  动作(比如数据库的插入，数据库的更新)
     * @return 总共操作了多少条
     */
    protected <ITEM> int batchHandle(List<ITEM> list, int pieSize, Function<List<ITEM>, Integer> action) {

        if (list == null || list.isEmpty()) {
            return 0;
        }

        int actionNum = 0;
        // 分批次插入，防止一次性插入或者更新的数据太多
        int batchCount = list.size() % pieSize == 0 ? list.size() / pieSize : list.size() / pieSize + 1;
        for (int batchIndex = 0; batchIndex < batchCount; batchIndex++) {
            int subStartIndex = batchIndex * pieSize;
            int subEndIndex = (batchIndex + 1) * pieSize;
            if (subEndIndex > list.size()) {
                subEndIndex = list.size();
            }
            actionNum += action.apply(list.subList(subStartIndex, subEndIndex));
        }
        return actionNum;
    }
}
