package com.tuacy.logback;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication()
public class LogBackApplication {

    private Logger logger = LoggerFactory.getLogger(LogBackApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(LogBackApplication.class, args);
        // 我们已经添加了spring-boot-starter-web，所以下面一段代码可以去掉了
        try {
            // 阻塞住，要不然一跑完就结束了，因为我这里没有添加starter-web
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
