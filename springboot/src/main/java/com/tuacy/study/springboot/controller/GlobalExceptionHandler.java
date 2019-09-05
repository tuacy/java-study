package com.tuacy.study.springboot.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @name: GlobalExceptionHandler
 * @author: tuacy.
 * @date: 2019/9/3.
 * @version: 1.0
 * @Description: 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // 你也可以在这个类里面对不同的异常做不同的处理


    /**
     * 处理所有不可知的异常
     */
    @ExceptionHandler(Exception.class)
    @ResponseBody
    String handleException(Exception e) {
        return "exception";
    }

}
