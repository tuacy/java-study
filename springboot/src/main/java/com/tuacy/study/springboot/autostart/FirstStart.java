package com.tuacy.study.springboot.autostart;

import com.tuacy.study.springboot.AutoStart;
import com.tuacy.study.springboot.IAutoStart;

/**
 * @name: FirstStart
 * @author: tuacy.
 * @date: 2019/8/16.
 * @version: 1.0
 * @Description:
 */
@AutoStart(des = "第一个启动")
public class FirstStart implements IAutoStart {

    @Override
    public void start(String des) {
        System.out.println("我启动了哈 " + des);
    }
}
