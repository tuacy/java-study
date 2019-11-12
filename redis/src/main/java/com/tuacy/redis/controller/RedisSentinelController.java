package com.tuacy.redis.controller;

import com.tuacy.redis.entity.request.GetRedis;
import com.tuacy.redis.entity.request.SetRedis;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @name: RedisSentinelController
 * @author: tuacy.
 * @date: 2019/11/11.
 * @version: 1.0
 * @Description:
 */
@RestController
@RequestMapping("/redis")
public class RedisSentinelController {

    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    public void setStringRedisTemplate(StringRedisTemplate stringRedisTemplate) {
        this.stringRedisTemplate = stringRedisTemplate;
    }

    @RequestMapping(value = "/getValue", method = RequestMethod.POST)
    public String sentinelGet(@RequestBody GetRedis param) {
        return stringRedisTemplate.opsForValue().get(param.getKey());
    }

    @RequestMapping(value = "/setValue", method = RequestMethod.POST)
    public String sentinelGet(@RequestBody SetRedis param) {
         stringRedisTemplate.opsForValue().set(param.getKey(), param.getValue());
         return "success";
    }

}
