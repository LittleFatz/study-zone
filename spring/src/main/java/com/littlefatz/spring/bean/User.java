package com.littlefatz.spring.bean;


import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;

public class User implements InitializingBean  {

    private String username;
    private int age;

    @Autowired
    private Vehicle vehicle;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public User() {
        System.out.println("normal construct");
    }



    public void print() {
        System.out.println("Car value:" + vehicle.getPrice());
        System.out.println(vehicle.hashCode());
    }

    @PostConstruct
    public void postConstruct() {
        System.out.println("post construct");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("afterPropertiesSet");
    }
}
