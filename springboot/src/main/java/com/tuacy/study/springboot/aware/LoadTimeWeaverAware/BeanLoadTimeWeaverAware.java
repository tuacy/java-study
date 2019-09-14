package com.tuacy.study.springboot.aware.LoadTimeWeaverAware;

import org.springframework.context.weaving.LoadTimeWeaverAware;
import org.springframework.instrument.classloading.LoadTimeWeaver;
import org.springframework.stereotype.Component;

@Component
public class BeanLoadTimeWeaverAware implements LoadTimeWeaverAware {

    private LoadTimeWeaver loadTimeWeaver;

    @Override
    public void setLoadTimeWeaver(LoadTimeWeaver loadTimeWeaver) {
        this.loadTimeWeaver = loadTimeWeaver;
    }


}
