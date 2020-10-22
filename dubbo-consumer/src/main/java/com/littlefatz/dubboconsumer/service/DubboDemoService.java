package com.littlefatz.dubboconsumer.service;

import com.littlefatz.dubbo.api.service.HelloService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DubboDemoService {

    @DubboReference(version = "1.0.0")
    private HelloService helloService;

    public String callProvider() {
        return helloService.hello("from consumer");
    }
}
