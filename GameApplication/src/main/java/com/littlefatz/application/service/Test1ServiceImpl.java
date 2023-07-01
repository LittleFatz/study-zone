package com.littlefatz.application.service;

import com.littlefatz.application.config.Test1Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test1ServiceImpl implements Test1Service {

    @Override
    public String hello(String name) {
        return name + " hello";
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Test1Configuration.class);


        Test1Service test1Service = (Test1Service) context.getBean("test1Service");
        Test2Service test2Service = (Test2Service) context.getBean("test2Service");
        System.out.println(test1Service.hello("stevie"));

        System.out.println(test2Service.hello("stevie"));
    }
}
