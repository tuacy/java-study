package com.tuacy.study.springboot.aware.ApplicationContextAware;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class LoadDbService implements ApplicationListener<ConfigFIleLoadSuccessEvent> {


    @Override
    public void onApplicationEvent(ConfigFIleLoadSuccessEvent event) {
        // 做相应的处理
    }
}
