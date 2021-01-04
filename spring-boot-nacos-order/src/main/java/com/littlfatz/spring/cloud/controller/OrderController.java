package com.littlfatz.spring.cloud.controller;

import com.littlfatz.spring.cloud.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @RequestMapping(value = "/order/{string}", method = RequestMethod.GET)
    public String order(@PathVariable String string) {
        return "Placing order:" + string;
    }

    @GetMapping(value = "/create")
    public String createOrder(
            @RequestParam("productId") Long productId,
            @RequestParam("userId") Long userId,
            @RequestParam("stockCount") Integer stockCount,
            @RequestParam("creditCount") Integer creditCount) {
        orderService.createOrder(productId, userId, stockCount, creditCount);
        return "success";
    }
}
