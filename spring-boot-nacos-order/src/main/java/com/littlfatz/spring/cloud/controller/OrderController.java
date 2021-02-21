package com.littlfatz.spring.cloud.controller;

import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.littlfatz.spring.cloud.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/order/{string}", method = RequestMethod.GET)
    @SentinelResource(value="order")
    public String order(@PathVariable String string) {
        return "Placing order:" + string;
    }

    @GetMapping(value = "/create")
    @SentinelResource(value="create")
    public String createOrder(
            @RequestParam("productId") Long productId,
            @RequestParam("userId") Long userId,
            @RequestParam("stockCount") Integer stockCount,
            @RequestParam("creditCount") Integer creditCount) {
        orderService.createOrder(productId, userId, stockCount, creditCount);
        return "success";
    }

    /**
     * 需要直接调用 restTemplate 的方法，才能调用 ribbon loadbalance interceptor
     * @param productId
     * @param userId
     * @param stockCount
     * @param creditCount
     * @return
     */
    @GetMapping(value = "/create2")
    @SentinelResource(value="create2")
    public String createOrder2(
            @RequestParam("productId") Long productId,
            @RequestParam("userId") Long userId,
            @RequestParam("stockCount") Integer stockCount,
            @RequestParam("creditCount") Integer creditCount) {

        restTemplate.getForEntity("http://stock-service/create?productId=1&userId=1&stockCount=1&creditCount=1",String.class);
        return "success";
    }

}
