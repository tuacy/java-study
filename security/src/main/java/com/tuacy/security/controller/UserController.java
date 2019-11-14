package com.tuacy.security.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @name: UserController
 * @author: tuacy.
 * @date: 2019/11/14.
 * @version: 1.0
 * @Description:
 */
@RestController
@RequestMapping("]user")
public class UserController {

    @GetMapping
    public String getUsers() {
        return "Hello Spring Security";
    }

}
