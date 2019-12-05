package com.tuacy.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @name: TestController
 * @author: tuacy.
 * @date: 2019/12/5.
 * @version: 1.0
 * @Description:
 */
@RestController
public class TestController {
    @GetMapping("/hi")
    public String hi(String name) {
        return "hi , " + name;
    }
}
