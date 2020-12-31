package com.littlfatz.spring.cloud.controller;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OrderController {

    @RequestMapping(value = "/order/{string}", method = RequestMethod.GET)
    public String order(@PathVariable String string) {
        return "Placing order:" + string;
    }
}
