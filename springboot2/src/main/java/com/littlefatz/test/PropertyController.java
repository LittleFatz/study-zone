package com.littlefatz.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyController {

    @Value("${my.field.test1}")
    private String myField;

    @GetMapping("/property")
    public String getProperty() {
        return myField;
    }
}
