package com.tuacy.study.springboot.aware.ServletConfigAware;

import org.springframework.stereotype.Component;
import org.springframework.web.context.ServletConfigAware;

import javax.servlet.ServletConfig;

@Component
public class BeanServletConfigAware implements ServletConfigAware {

    private ServletConfig servletConfig;

    @Override
    public void setServletConfig(ServletConfig servletConfig) {
        this.servletConfig = servletConfig;
    }
}
