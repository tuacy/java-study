package com.tuacy.excel.write;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @name: ComplexHeadData
 * @author: tuacy.
 * @date: 2019/11/25.
 * @version: 1.0
 * @Description:
 */
@Data
public class ComplexHeadData {
    @ExcelProperty({"主标题", "字符串标题"})
    private String string;
    @ExcelProperty({"主标题", "日期标题"})
    private Date date;
    @ExcelProperty({"主标题", "数字标题"})
    private Double doubleData;
}
