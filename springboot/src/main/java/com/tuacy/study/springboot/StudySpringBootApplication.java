package com.tuacy.study.springboot;

import com.tuacy.study.springboot.hook.classPathBeanDefinitionScanner.BeanIocScan;
import com.tuacy.study.springboot.hook.importBeanDefinitionRegistrar.RunStartScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
// 会去这个包下面查找添加饿了RunStart注解的类，执行指定的方法
@RunStartScan(basePackages = {"com.tuacy.study.springboot.hook.importBeanDefinitionRegistrar.runstart"})
@BeanIocScan(basePackages = {"com.tuacy.study.springboot.hook.classPathBeanDefinitionScanner.autoioc"})
public class StudySpringBootApplication {

    public static void main(String[] args) {
        SpringApplication.run(StudySpringBootApplication.class, args);

        try {
            // 阻塞住，要不然一跑完就结束了，因为我这里没有添加starter-web
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
