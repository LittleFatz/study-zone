package com.littlefatz.application.config;

import com.littlefatz.application.service.Test1Service;
import com.littlefatz.application.service.Test1ServiceImpl;
import com.littlefatz.application.service.Test2Service;
import com.littlefatz.application.service.Test2ServiceImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Test1Configuration {

    @Bean
    public Test1Service test1Service() {
        System.out.println("init 1");
        return new Test1ServiceImpl();
    }

    @Configuration
//    @ConditionalOnClass(Test1Service.class)
    @ConditionalOnBean(name = "test1Service")//、、bean的话就会报错
    static class Test2Configuration {

        @Bean
        public Test2Service test2Service() {
            System.out.println("init 1");

            return new Test2ServiceImpl();
        }

    }
}
