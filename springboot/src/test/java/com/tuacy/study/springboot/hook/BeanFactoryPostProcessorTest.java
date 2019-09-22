package com.tuacy.study.springboot.hook;

import com.tuacy.study.springboot.hook.beanFactoryPostProcessor.HelloFactoryPostProcessorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class BeanFactoryPostProcessorTest {

    private HelloFactoryPostProcessorService helloFactoryPostProcessorService;

    @Autowired
    public void setHelloFactoryPostProcessorService(HelloFactoryPostProcessorService helloFactoryPostProcessorService) {
        this.helloFactoryPostProcessorService = helloFactoryPostProcessorService;
    }

    @Test
    public void test() {
        helloFactoryPostProcessorService.sayHello();
    }

}
