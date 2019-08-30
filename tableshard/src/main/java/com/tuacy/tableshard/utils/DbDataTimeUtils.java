package com.tuacy.tableshard.utils;

import java.time.LocalDateTime;

/**
 * @name: DbDataTimeUtils
 * @author: tuacy.
 * @date: 2019/8/30.
 * @version: 1.0
 * @Description:
 */
public class DbDataTimeUtils {

    /**
     * 日期类型转换为包含“年/月/日/时/分/秒/毫秒”的long型数据。
     * 日期范围：2000-1-1 ~ 2099-1-1，与DateTimeMinValue、DateTimeMaxValue一致。
     * 示例：2015-02-12 23:03:59.321 转换目标为：20150212230359321
     */
    public static long dateTime2Long(LocalDateTime tm) {
        return tm.getNano() / 1000000 +
                1000L * tm.getSecond() +
                100000L * tm.getMinute() +
                10000000L * tm.getHour() +
                1000000000L * tm.getDayOfMonth() +
                100000000000L * tm.getMonthValue() +
                10000000000000L * tm.getYear();
    }

    /**
     * 包含“年/月/日/时/分/秒/毫秒”的BCD码转换为日期类型。
     * 日期范围：2000-1-1 ~ 2099-1-1，与DateTimeMinValue、DateTimeMaxValue一致。
     * 示例：20150212230359321 转换目标为：2015-02-12 23:03:59.321
     */
    public static LocalDateTime long2DateTime(long val) {

        long year = val / 10000000000000L;

        long month = (val / 100000000000L) % 100L;

        long day = (val / 1000000000L) % 100L;

        long hour = (val / 10000000L) % 100L;

        long minute = (val / 100000L) % 100L;

        long second = (val / 1000L) % 100L;

        long millSecond = val % 1000L;

        return LocalDateTime.of((int) year, (int) month, (int) day, (int) hour, (int) minute, (int) second, (int) millSecond);
    }

}
