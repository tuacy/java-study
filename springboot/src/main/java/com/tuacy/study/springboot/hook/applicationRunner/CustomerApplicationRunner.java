package com.tuacy.study.springboot.hook.applicationRunner;

import com.tuacy.study.springboot.hook.applicationListener.CustomerEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @name: CustomeApplicationRunner
 * @author: tuacy.
 * @date: 2019/8/27.
 * @version: 1.0
 * @Description:
 */
@Component
public class CustomerApplicationRunner implements ApplicationRunner {

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("-------- ApplicationRunner ---------- ");
        applicationContext.publishEvent(new CustomerEvent("ApplicationRunner"));
    }
}
