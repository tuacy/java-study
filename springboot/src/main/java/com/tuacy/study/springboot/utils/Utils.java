package com.tuacy.study.springboot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Utils {

    private static final Logger logger = LoggerFactory.getLogger(Utils.class);

    /**
     * 打印当前线程堆栈信息
     *
     * @param prefix
     */
    public static void printTrack(String prefix) {
        StackTraceElement[] st = Thread.currentThread().getStackTrace();

        StringBuilder sbf = new StringBuilder();

        for (StackTraceElement e : st) {
            if (sbf.length() > 0) {
                sbf.append(" <- ");
                sbf.append(System.getProperty("line.separator"));
            }

            sbf.append(java.text.MessageFormat.format("{0}.{1}() {2}"
                    , e.getClassName()
                    , e.getMethodName()
                    , e.getLineNumber()));
        }

        logger.info(prefix
                + "\n************************************************************\n"
                + sbf.toString()
                + "\n************************************************************");
    }

}
