package com.tuacy.excel.write;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @name: LongestMatchColumnWidthData
 * @author: tuacy.
 * @date: 2019/11/25.
 * @version: 1.0
 * @Description:
 */
@Data
public class LongestMatchColumnWidthData {
    @ExcelProperty("字符串标题")
    private String string;
    @ExcelProperty("日期标题很长日期标题很长日期标题很长很长")
    private Date date;
    @ExcelProperty("数字")
    private Double doubleData;
}
