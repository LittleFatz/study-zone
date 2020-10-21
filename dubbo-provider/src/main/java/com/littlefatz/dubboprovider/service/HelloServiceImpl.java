package com.littlefatz.dubboprovider.service;

import com.littlefatz.dubbo.api.service.HelloService;

public class HelloServiceImpl implements HelloService {
    @Override
    public String hello(String message) {
        return "hello " + message;
    }
}
