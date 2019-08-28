package com.tuacy.study.springboot.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @ModelAttribute
    public void postModelAttribute(@RequestParam String abc, Model model) {
        model.addAttribute("attributeName", abc);
    }

    @RequestMapping(value = "/helloWorld")
    public String helloWorld() {
        return "helloWorld";
    }

}
