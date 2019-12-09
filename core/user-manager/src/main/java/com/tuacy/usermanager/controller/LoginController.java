package com.tuacy.usermanager.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @name: LoginController
 * @author: tuacy.
 * @date: 2019/12/9.
 * @version: 1.0
 * @Description:
 */
@RestController
@RequestMapping(value = "/user/info")
public class LoginController {

    @RequestMapping(value = "/my", method = RequestMethod.GET)
    public String myDetail() {
        Map curUser = (Map) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        String userName = (String) curUser.get("username");
        return userName;
    }
}
