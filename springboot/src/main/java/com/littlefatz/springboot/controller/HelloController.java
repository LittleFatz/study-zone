package com.littlefatz.springboot.controller;

import com.littlefatz.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private UserService userService;

    @RequestMapping(method = RequestMethod.GET, path = "/print")
    @ResponseBody
    public String hello() {
        System.out.println("hello world");

        return "hello world";
    }
}
