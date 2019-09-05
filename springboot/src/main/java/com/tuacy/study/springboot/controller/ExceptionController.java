package com.tuacy.study.springboot.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @name: ExceptionController
 * @author: tuacy.
 * @date: 2019/9/3.
 * @version: 1.0
 * @Description:
 */
@RestController
@RequestMapping(path = "/exception")
public class ExceptionController {

    /**
     * 其实我们也可以直接拿到Model
     */
    @RequestMapping(value = "/text")
    public String helloWorld() {
        // 这里我们做一个简单的测试，人为的去抛出一一个异常
        throw new NullPointerException("ababa");
//        return "helloWorld";
    }

}
