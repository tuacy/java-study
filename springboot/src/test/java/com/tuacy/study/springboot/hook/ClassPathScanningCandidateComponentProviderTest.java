package com.tuacy.study.springboot.hook;

import com.tuacy.study.springboot.hook.importSelector.HelloService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.core.type.filter.TypeFilter;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@SpringBootTest()
public class ClassPathScanningCandidateComponentProviderTest {

    /**
     * 搜索指定包(com.tuacy.study.springboot.hook.importSelector)下,实现了HelloService接口的类
     */
    @Test
    public void test() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        TypeFilter helloServiceFilter = new AssignableTypeFilter(HelloService.class);
        scanner.addIncludeFilter(helloServiceFilter);
        Set<BeanDefinition> classes = scanner.findCandidateComponents("com.tuacy.study.springboot.hook.importSelector");
        if (!classes.isEmpty()) {
            classes.forEach(beanDefinition -> System.out.println(beanDefinition.getBeanClassName()));
        }

    }

}
