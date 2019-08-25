package com.tuacy.study.springboot.hook;

import com.tuacy.study.springboot.hook.beanpostprocessor.TestController;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class BeanPostProcessorTest {

    private TestController testController;

    @Autowired
    public void setTestController(TestController testController) {
        this.testController = testController;
    }


    @Test
    public void test() {
        testController.test();
    }

}
