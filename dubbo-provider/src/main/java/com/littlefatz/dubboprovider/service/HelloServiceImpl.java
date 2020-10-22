package com.littlefatz.dubboprovider.service;

import com.littlefatz.dubbo.api.service.HelloService;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;

@DubboService(version = "1.0.0")
public class HelloServiceImpl implements HelloService {

    @Override
    public String hello(String message) {
        return "hello " + message;
    }
}
