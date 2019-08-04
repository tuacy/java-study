package com.tuacy.study.distributelock.utils;

public class ThreadUtil {

    private ThreadUtil() {

    }

    // 当前线程的UUID信息，主要用于打印日志；
    private static ThreadLocal<String> currThreadUuid = ThreadLocal.withInitial(UUIDGeneratorUtil::getUUID);

    public static String getCurrThreadUuid() {
        return currThreadUuid.get();
    }

}
