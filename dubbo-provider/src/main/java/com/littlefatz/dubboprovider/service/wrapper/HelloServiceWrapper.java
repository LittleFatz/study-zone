package com.littlefatz.dubboprovider.service.wrapper;

import com.littlefatz.dubbo.api.service.HelloService;
import org.apache.dubbo.common.URL;

public class HelloServiceWrapper implements HelloService {

    private HelloService helloService;

    public HelloServiceWrapper(HelloService helloService) {
        this.helloService = helloService;
    }

    @Override
    public String hello(String message) {
        System.out.println("wrap");
        return helloService.hello(message);
    }

    @Override
    public String hello(String message, URL url) {
        System.out.println("wrap");
        return helloService.hello(message, url);
    }
}
