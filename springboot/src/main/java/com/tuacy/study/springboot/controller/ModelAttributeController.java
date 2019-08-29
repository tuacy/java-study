package com.tuacy.study.springboot.controller;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;
import com.google.common.io.Files;
import com.tuacy.study.springboot.configinject.ProductInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.List;
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
     * 注入普通字符串,相当于private String normal = "normal"
     */
    @Value("normal")
    private String normal;

    /**
     * 注入操作系统属性
     */
    @Value("#{systemProperties['os.name']}")
    private String systemPropertiesName;

    /**
     * 注入表达式结果,相当于 double randomNumber = java.lang.Math).random() * 100.0
     */
    @Value("#{ T(java.lang.Math).random() * 100.0 }")
    private double randomNumber;

    /**
     * 注入其他Bean属性：相当于把IOC容器里面valueInject名字对应的对象的name属性赋值给变量
     */
    @Value("#{valueInject.name}")
    private String fromAnotherBean;

    /**
     * 注入文件资源,相当于把resource目录下valueInjectConfig.txt文件注入进来
     */
    @Value("classpath:valueInjectConfig.txt")
    private Resource valueInjectConfig;

    /**
     * 注入URL资源，相当于把http://www.baidu.com对应的资源注入进来
     */
    @Value("http://www.baidu.com")
    private Resource baiduUrl;

    private ProductInfo productInfo;

    @Autowired
    @Qualifier(value = "productInfo")
    public void setProductInfo(ProductInfo productInfo) {
        this.productInfo = productInfo;
    }

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
