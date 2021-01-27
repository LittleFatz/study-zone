package com.littlefatz.dubboprovider.service;

import com.littlefatz.dubbo.api.service.EchoService;
import org.apache.dubbo.common.extension.Activate;

@Activate(group = {"animal"},value = "lion-animal")
public class LionEchoService implements EchoService {

    @Override
    public void echo(String s) {
        System.out.println("lion:" + s);
    }
}
