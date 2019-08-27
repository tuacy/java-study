package com.tuacy.study.springboot.beanloadorder;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @name: BeanLoderOrderConfig
 * @author: tuacy.
 * @date: 2019/8/27.
 * @version: 1.0
 * @Description:
 */
@Configuration
public class BeanLoderOrderConfig {

    @Bean(initMethod = "init")
    @DependsOn("orderFirstBean")
    public OrderSecondBean orderSecondBean() {
        return new OrderSecondBean();
    }

    @Bean(initMethod = "init")
    public OrderFirstBean orderFirstBean() {
        return new OrderFirstBean();
    }


}
