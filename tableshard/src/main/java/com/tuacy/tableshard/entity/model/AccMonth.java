package com.tuacy.tableshard.entity.model;

import com.tuacy.tableshard.mapper.CreateTableMapper;
import com.tuacy.tableshard.tableextend.tableshard.TableCreate;
import lombok.Getter;
import lombok.Setter;

@TableCreate(
        tableName = "StatisAccMonth",
        autoCreateTableMapperClass = CreateTableMapper.class,
        autoCreateTableMapperMethodName = "createAccMonth"
)
@Getter // lombok 注解,不用手动去写get set方法
@Setter
public class AccMonth {

    private Long recTime;
    private Long ptId;
    private Double value;

}
