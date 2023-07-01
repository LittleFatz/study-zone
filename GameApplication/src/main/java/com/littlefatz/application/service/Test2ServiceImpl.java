package com.littlefatz.application.service;

import com.littlefatz.application.config.Test1Configuration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Test2ServiceImpl implements Test2Service {

    @Override
    public String hello(String name) {
        return name + " hello22222";
    }

    public static void main(String[] args) {
        ApplicationContext context = new AnnotationConfigApplicationContext(Test1Configuration.class);


        Test1Service test1Service = (Test1Service) context.getBean("test1Service");
        System.out.println(test1Service.hello("stevie"));
    }
}
