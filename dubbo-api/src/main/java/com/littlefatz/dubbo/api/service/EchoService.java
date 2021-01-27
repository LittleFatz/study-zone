package com.littlefatz.dubbo.api.service;

import org.apache.dubbo.common.extension.SPI;

@SPI("wolf")
public interface EchoService {

    void echo(String s);
}
