package com.tuacy.study.springboot.hook;

import com.tuacy.study.springboot.hook.instantiationAwareBeanPostProcessor.TestInstantiationAwareBeanPostProcessor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @name: InstantiationAwareBeanPostProcessorTest
 * @author: tuacy.
 * @date: 2019/9/25.
 * @version: 1.0
 * @Description:
 */
@RunWith(SpringRunner.class)
@SpringBootTest()
public class InstantiationAwareBeanPostProcessorTest {

    private TestInstantiationAwareBeanPostProcessor testInstantiationAwareBeanPostProcessor;

    @Autowired
    public void setTestInstantiationAwareBeanPostProcessor(TestInstantiationAwareBeanPostProcessor testInstantiationAwareBeanPostProcessor) {
        this.testInstantiationAwareBeanPostProcessor = testInstantiationAwareBeanPostProcessor;
    }

    @Test
    public void test() {
        this.testInstantiationAwareBeanPostProcessor.dosomething();
    }

}
