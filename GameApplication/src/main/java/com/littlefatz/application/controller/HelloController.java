package com.littlefatz.application.controller;

import com.littlefatz.application.service.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private MainService mainService;

    @RequestMapping(method = RequestMethod.GET, path = "/print")
    @ResponseBody
    public String hello() {
        String detail = mainService.getDetail();
        System.out.println(detail);
        return detail;
    }

}
