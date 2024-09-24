package com.cy.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.cy.service.HelloService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author：SatanCY
 * @Date：2024/9/24 10:57
 */
@Controller
@RequestMapping("/demo")
public class HelloController {

    @Reference // 到注册中心去查找对应服务并订阅
    private HelloService helloService;

    @GetMapping("/hello")
    @ResponseBody
    public String sayHello(String name) {
        String str = helloService.sayHello(name);
        return "<h1>" + str + "</h1>";
    }
}
