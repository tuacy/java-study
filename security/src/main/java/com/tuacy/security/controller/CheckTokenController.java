package com.tuacy.security.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * @name: CheckTokenController
 * @author: tuacy.
 * @date: 2019/12/7.
 * @version: 1.0
 * @Description:
 */
@RestController
public class CheckTokenController {
    @RequestMapping("/user")
    public Principal user(Principal user) {
        return user;
    }
}
