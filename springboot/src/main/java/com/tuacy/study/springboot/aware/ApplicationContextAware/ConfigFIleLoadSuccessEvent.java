package com.tuacy.study.springboot.aware.ApplicationContextAware;

import org.springframework.context.ApplicationEvent;

public class ConfigFIleLoadSuccessEvent extends ApplicationEvent {
    /**
     * Create a new ApplicationEvent.
     *
     * @param source the object on which the event initially occurred (never {@code null})
     */
    public ConfigFIleLoadSuccessEvent(Object source) {
        super(source);
    }
}
