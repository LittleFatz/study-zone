package com.littlefatz.service;

import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public int getUser() {
        System.out.println("service:   working");
        return 10;
    }
}
