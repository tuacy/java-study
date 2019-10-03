package com.tuacy.study.springboot.aware.EnvironmentAware;

import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class BeanEnvironmentAware implements EnvironmentAware {

    /**
     * 其实这里咱们完全可以不使用EnvironmentAware接口，直接通过@Autowired把Environment注入进来也是一样的
     */
    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
