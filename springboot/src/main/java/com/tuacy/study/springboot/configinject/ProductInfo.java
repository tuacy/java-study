package com.tuacy.study.springboot.configinject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * 注入resource下product.properties文件里面的信息
 */
@Configuration
@PropertySource(value = "classpath:product.properties", encoding = "utf-8")
public class ProductInfo {

    @Value("${ppid}")
    private int pid;


    @Value("${mmid}")
    private int mid;


    @Value("${ccid}")
    private int cid;

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getMid() {
        return mid;
    }

    public void setMid(int mid) {
        this.mid = mid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
