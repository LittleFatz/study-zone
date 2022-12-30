package com.littlefatz.application.dubboconsumer.controller;


import com.littlefatz.application.dubboconsumer.service.DubboDemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private DubboDemoService dubboDemoService;

    //http://127.0.0.1:8881/consumer/hello/print
    @RequestMapping(method = RequestMethod.GET, path = "/print")
    @ResponseBody
    public String hello() {
        String response = dubboDemoService.callProvider();
        System.out.println(response);
        return response;
    }
}
