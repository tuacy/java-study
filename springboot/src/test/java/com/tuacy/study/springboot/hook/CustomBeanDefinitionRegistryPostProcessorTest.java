package com.tuacy.study.springboot.hook;

import com.tuacy.study.springboot.entity.CustomBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @name: CustomBeanDefinitionRegistryPostProcessorTest
 * @author: tuacy.
 * @date: 2019/8/22.
 * @version: 1.0
 * @Description:
 */

@RunWith(SpringRunner.class)
@SpringBootTest()
public class CustomBeanDefinitionRegistryPostProcessorTest {

    private CustomBean customBean;

    @Autowired
    public void setCustomBean(CustomBean customBean) {
        this.customBean = customBean;
    }

    @Test
    public void testBeanDefinition() {
        System.out.println(customBean.getName());
    }
}
