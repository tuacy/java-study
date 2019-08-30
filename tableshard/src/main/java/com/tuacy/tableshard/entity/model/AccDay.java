package com.tuacy.tableshard.entity.model;

import com.tuacy.tableshard.mapper.CreateTableMapper;
import com.tuacy.tableshard.tableextend.tableshard.TableCreate;
import lombok.Getter;
import lombok.Setter;

@TableCreate(
        tableName = "StatisAccDay",
        autoCreateTableMapperClass = CreateTableMapper.class,
        autoCreateTableMapperMethodName = "createAccDay"
)
@Getter // lombok 注解,不用手动去写get set方法
@Setter
public class AccDay {

    private Long recTime;
    private Long ptId;
    private Double value;

}
