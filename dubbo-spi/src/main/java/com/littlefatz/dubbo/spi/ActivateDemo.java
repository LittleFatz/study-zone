package com.littlefatz.dubbo.spi;

import com.littlefatz.dubbo.api.service.EchoService;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionLoader;

import java.util.List;

public class ActivateDemo {

    public static void main(String[] args) {
        ActivateDemo demo = new ActivateDemo();
        demo.testGroupAndKey();
    }

    public void testGroupAndKey() {
        ExtensionLoader<EchoService> loader = ExtensionLoader.getExtensionLoader(EchoService.class);
        URL url = URL.valueOf("test://localhost/test");

        List<EchoService> list = loader.getActivateExtension(url, new String[]{"lion"}, "animal");

        System.out.println(list.size());

        for (EchoService service : list) {
            service.echo("hi");
        }

    }

    public void testKey() {
        ExtensionLoader<EchoService> loader = ExtensionLoader.getExtensionLoader(EchoService.class);
        URL url = URL.valueOf("test://localhost/test");

        List<EchoService> list = loader.getActivateExtension(url, new String[]{"lion"}, "animal");

        System.out.println(list.size());

        for (EchoService service : list) {
            service.echo("hi");
        }

    }
}
