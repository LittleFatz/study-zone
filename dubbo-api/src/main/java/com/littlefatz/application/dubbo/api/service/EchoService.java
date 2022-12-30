package com.littlefatz.application.dubbo.api.service;

import org.apache.dubbo.common.extension.SPI;

@SPI
public interface EchoService {

    void echo(String s);
}
