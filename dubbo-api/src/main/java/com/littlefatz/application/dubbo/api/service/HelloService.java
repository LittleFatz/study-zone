package com.littlefatz.application.dubbo.api.service;

import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

@SPI("boy")
public interface HelloService {

    String hello(String message);

    @Adaptive
    String hello(String message, URL url);
}
