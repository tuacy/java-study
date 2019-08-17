package com.tuacy.study.license;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class LicenseApplication {

    public static void main(String[] args) {
        SpringApplication.run(LicenseApplication.class, args);

        try {
            // 阻塞住，要不然一跑完就结束了，因为我这里没有添加starter-web
            Thread.currentThread().join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
