package com.tuacy.study.springboot.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @name: ModelAttributeController
 * @author: tuacy.
 * @date: 2019/8/28.
 * @version: 1.0
 * @Description:
 */
@RestController
@RequestMapping(path = "/modelAttribute")
public class ModelAttributeController {

    /**
     * Model: 就相当于每次请求的一个背包，我们可以往背包里面放东西
     * 会在其他添加了@RequestMapping的方法之前执行
     */
    @ModelAttribute
    public void postVoidModelAttribute(@RequestParam String abc, Model model) {
        // 往model里面添加一个属性
        model.addAttribute("userId0", abc);
    }

    /**
     * Model: 就相当于每次请求的一个背包，我们可以往背包里面放东西
     * @ModelAttribute 添加在有返回值的放的方法上，会把返回值放到Model里面去
     * 会在其他添加了@RequestMapping的方法之前执行
     */
    @ModelAttribute(value = "userId1")
    public String postReturnModelAttribute(@RequestParam String abc) {
        return abc;
    }

    /**
     * 上面我们已经通过把@ModelAttribute添加在方法上往Model里面去了，这个时候我们可以通过把@ModelAttribute添加在参数上获取Model里面的值
     */
    @RequestMapping(value = "/text0")
    public String test0(@RequestParam String abc, @ModelAttribute(name = "userId0", binding = false) String userId) {
        return "helloWorld";
    }

    /**
     * 其实我们也可以直接拿到Model
     */
    @RequestMapping(value = "/text1")
    public String helloWorld(@RequestParam String abc, Model model) {
        Map<String, Object> mapList = model.asMap();
        return "helloWorld";
    }

}
