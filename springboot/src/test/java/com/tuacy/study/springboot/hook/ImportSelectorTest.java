package com.tuacy.study.springboot.hook;

import com.tuacy.study.springboot.hook.importSelector.HelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest()
public class ImportSelectorTest {

    private List<HelloService> helloServices;

    @Autowired
    public void setHelloServices(List<HelloService> helloServices) {
        this.helloServices = helloServices;
    }

    @Test
    public void test() {
        this.helloServices.forEach(helloService -> helloService.doSomething());
    }
}
