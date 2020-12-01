package com.littlefatz.dubbo.spi;

import com.littlefatz.dubbo.api.service.HelloService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;

public class DubboSPIDemo {

    public static void main(String[] args) {
        URL url = URL.valueOf("test://localhost/hello?hello.service=girl");
        final HelloService adaptiveExtension = ExtensionLoader.getExtensionLoader(HelloService.class).getAdaptiveExtension();
        System.out.println(adaptiveExtension.hello("hello there", url));
    }
}
