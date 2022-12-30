package com.littlefatz.application.dubboprovider.service.wrapper;

import com.littlefatz.application.dubbo.api.service.HelloService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService(version = "1.0.0")
public class GirlHelloService implements HelloService {

    @Override
    public String hello(String message) {
        return "girl: " + message;
    }

    @Override
    public String hello(String message, URL url) {
        return "girl: " + message;
    }
}
