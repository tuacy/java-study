package com.tuacy.study.springboot.controller;

import com.tuacy.study.springboot.entity.InitBinderTestVo;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @name: InitBinderController
 * @author: tuacy.
 * @date: 2019/9/5.
 * @version: 1.0
 * @Description:
 */
@RestController
@RequestMapping(path = "/initBinder")
public class InitBinderController {

    /**
     * 对当前控制器的所有请求都有效
     * 前台传递过来的String类型时间，通过下面的初始化绑定，转换成Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // 我们前台传过来的time字段对应的值可能是“2019-05-06”这样的，我们可以在这里把他们转换成Date类型的
        binder.registerCustomEditor(Date.class, new CustomDateEditor(new SimpleDateFormat("yyyy-MM-dd"), true));
    }


    @RequestMapping(value = "/test", method = RequestMethod.POST)
    public String helloWorld(@RequestBody InitBinderTestVo vo) {
        System.out.println(vo.getTime());
        return "success";
    }

}
