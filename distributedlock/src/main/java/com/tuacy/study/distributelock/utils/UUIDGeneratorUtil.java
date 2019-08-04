package com.tuacy.study.distributelock.utils;

import java.util.UUID;

public class UUIDGeneratorUtil {


    private UUIDGeneratorUtil() {

    }

    /**
     * 获取32位UUID字符串
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 获取32位UUID大写字符串 
     */
    public static String getUpperCaseUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "").toUpperCase();
    }

}
