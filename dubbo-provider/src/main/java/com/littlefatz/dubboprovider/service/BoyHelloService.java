package com.littlefatz.dubboprovider.service;

import com.littlefatz.dubbo.api.service.HelloService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;
import org.apache.dubbo.config.annotation.Service;

@DubboService(version = "1.0.0")
public class BoyHelloService implements HelloService {

    @Override
    public String hello(String message) {
        return "boy: " + message;
    }

    @Override
    public String hello(String message, URL url) {
        return "boy: " + message;
    }
}
