package com.cy.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.cy.service.HelloService;

/**
 * @Author：SatanCY
 * @Date：2024/9/24 10:45
 */
@Service // 发布服务必须使用Dubbo提供的Service注解
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }
}
