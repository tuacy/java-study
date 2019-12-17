package com.tuacy.securityoauth;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

/**
 * @name: SecurityApplication
 * @author: tuacy.
 * @date: 2019/11/14.
 * @version: 1.0
 * @Description:
 */
@SpringBootApplication
@MapperScan(basePackages = "com.tuacy.securityoauth.mapper")
@EnableEurekaClient
@EnableDiscoveryClient
@EnableCaching
public class SecurityOauthServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecurityOauthServerApplication.class, args);
    }

}
