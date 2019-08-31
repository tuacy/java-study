package com.tuacy.tableshard.entity.model;

import com.tuacy.tableshard.mapper.CreateTableMapper;
import com.tuacy.tableshard.tableextend.tableshard.TableCreate;
import lombok.Getter;
import lombok.Setter;

@TableCreate(
        tableName = "StatisAccHour",
        autoCreateTableMapperClass = CreateTableMapper.class,
        autoCreateTableMapperMethodName = "createAccHour"
)
@Getter // lombok 注解,不用手动去写get set方法
@Setter
public class AccHour {

    /**
     * 针对recTime做一个简单说明,
     * 比如当前时间是 2019年08月31日00时31分46秒141微妙
     * 则我们在数据库里面存20190831003146141
     */
    private Long recTime;
    private Long ptId;
    private Double value;

}
