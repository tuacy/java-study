package com.tuacy.excel.entity;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @name: WriteModel
 * @author: tuacy.
 * @date: 2019/11/25.
 * @version: 1.0
 * @Description:
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WriteModel {

    @ExcelProperty(value = "姓名", index = 0)
    private String name;

    @ExcelProperty(value = "密码", index = 1)
    private String password;

    @ExcelProperty(value = "年龄", index = 2)
    private Integer age;
}
