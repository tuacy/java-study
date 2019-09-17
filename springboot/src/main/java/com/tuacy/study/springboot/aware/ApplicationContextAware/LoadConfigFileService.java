package com.tuacy.study.springboot.aware.ApplicationContextAware;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.stereotype.Service;

@Service
public class LoadConfigFileService implements ApplicationEventPublisherAware {

    private ApplicationEventPublisher applicationEventPublisher;


    /**
     * 我们模拟一个这样的场景,比如,我们Spring启动的时候需要加载两部分的配置
     * 1. 配置文件里面的配置
     * 2. 数据库里面的配置
     * 但是这两部分的配置我们又是在两个Service里面实现的.
     */
    public boolean loadConfigFIle() {

        // TODO:加载配置文件里面的配置

        // 发布事件
        applicationEventPublisher.publishEvent(new ConfigFIleLoadSuccessEvent("ConfigFIleLoadSuccessEvent"));

        return true;
    }

    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;

    }
}
