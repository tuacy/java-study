package com.tuacy.study.springboot.hook.smartLifecycle;

import org.springframework.context.SmartLifecycle;

public class CustomizeLifeCycleLinstener implements SmartLifecycle {
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isRunning() {
        return false;
    }
}
