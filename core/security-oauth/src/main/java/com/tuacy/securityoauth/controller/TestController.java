package com.tuacy.securityoauth.controller;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @name: TestController
 * @author: tuacy.
 * @date: 2019/12/5.
 * @version: 1.0
 * @Description:
 */
@RestController
public class TestController {

    @GetMapping("/redisOauth")
    public String redisOauth(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return "ok";
    }


    @GetMapping("/jwtOauth")
    public String jwtOauth(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        Claims body = Jwts.parser().setSigningKey("123".getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token).getBody();

        String username = (String) body.get("username");
        return "ok";
    }

}
