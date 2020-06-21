package com.littlefatz.test;

import com.littlefatz.spring.statemachine.OrderEvent;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @RequestMapping("/")
    public String index() {
        return "Greetings from Spring Boot!";
    }

    @PostMapping("/stateMachine")
    public String stateMachine(@RequestParam OrderEvent event) {
        return "done";

    }

}