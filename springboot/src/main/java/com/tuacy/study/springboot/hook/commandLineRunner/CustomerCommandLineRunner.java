package com.tuacy.study.springboot.hook.commandLineRunner;

import com.tuacy.study.springboot.hook.applicationListener.CustomerEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @name: CustomerCommandLineRunner
 * @author: tuacy.
 * @date: 2019/8/27.
 * @version: 1.0
 * @Description:
 */
@Component
public class CustomerCommandLineRunner implements CommandLineRunner {

    private ApplicationContext applicationContext;

    @Autowired
    public void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("-------- CommandLineRunner ---------- ");
        applicationContext.publishEvent(new CustomerEvent("CommandLineRunner"));
    }
}
