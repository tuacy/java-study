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
@AutoStart(des = "第二个启动", oder = 1)
public class SecondStart implements IAutoStart {

    @Override
    public void start(String des) {
        System.out.println("我启动了哈 " + des);
    }
}
