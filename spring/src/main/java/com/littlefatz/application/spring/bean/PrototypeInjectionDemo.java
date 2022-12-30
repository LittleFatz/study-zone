package com.littlefatz.application.spring.bean;


import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PrototypeInjectionDemo {

    public static void main(String[] args) {

        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        applicationContext.register(PrototypeInjectionDemo.class);

        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(applicationContext);
        String path = "classpath:META-INF/prototype-injection.xml";

        reader.loadBeanDefinitions(path);
        applicationContext.refresh();

//        Vehicle vehicle = (Vehicle) applicationContext.getBean("vehicle");
        User user = (User) applicationContext.getBean("user");
        user.print();

        user = (User) applicationContext.getBean("user");
        user.print();

        user = (User) applicationContext.getBean("user");
        user.print();

        applicationContext.close();

    }

}
