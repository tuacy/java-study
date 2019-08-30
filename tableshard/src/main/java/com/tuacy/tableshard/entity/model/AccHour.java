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

    private Long recTime;
    private Long ptId;
    private Double value;

}
