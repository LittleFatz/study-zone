package com.littlefatz.application.dubboconsumer.service;

import com.littlefatz.application.dubbo.api.service.HelloService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Service;

@Service
public class DubboDemoService {

    @DubboReference(version = "1.0.0",check = false, mock = "return null")
    private HelloService helloService;

    public String callProvider() {
        return helloService.hello("from consumer");
    }

}
