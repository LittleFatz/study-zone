package com.littlefatz.application.springboot.controller;

import com.littlefatz.application.springboot.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/hello")
public class HelloController {

    @Autowired
    private UserService userService;

//    @Autowired
//    private RedisTemplate<String, Object> redisTemplate;

    private final String BITMAP_KEY = "bitmap_test";

    @RequestMapping(method = RequestMethod.GET, path = "/print")
    @ResponseBody
    public String hello() {
        System.out.println("hello world");

        return "hello world";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/jvm1")
    @ResponseBody
    public String jvm() {
        List<Integer> list = new ArrayList<>();
        boolean flag = true;
        while (flag) {
            list.add(1);
        }

        return "done";
    }

//
//    @RequestMapping(method = RequestMethod.POST, path = "/setbit/{position}")
//    @ResponseBody
//    public boolean setBitMap(@PathVariable int position) {
//        redisTemplate.opsForValue().setBit(BITMAP_KEY, position, true);
//        return true;
//    }
//
//    @RequestMapping(method = RequestMethod.GET, path = "/getbit")
//    @ResponseBody
//    public String getRedis() {
//
//        String execute = redisTemplate.execute((RedisCallback<String>) conn -> {
//            byte[] bytes = conn.get(BITMAP_KEY.getBytes());
//            String s = convertByteArraysToBinary(bytes);
//            System.out.println(s);
//            return s;
//        });
//
//
////        redisTemplate.opsForValue().getRedis
////        redisTemplate.opsForValue().bit
////        String o = (String) redisTemplate.opsForValue().get(BITMAP_KEY);
////        byte[] bytes = o.getBytes();
////        String s = convertByteArraysToBinary(bytes);
////        System.out.println(s);
//        return execute;
//    }

    public static String convertByteArraysToBinary(byte[] input) {
        final StringBuilder result = new StringBuilder();
        for (byte b : input) {
            int val = b; // byte -> int
            for (int i = 0; i < 8; i++) {
                result.append((val & 128) == 0 ? 0 : 1);      // 128 = 1000 0000
                val <<= 1;
            }
        }
        return result.toString();
    }
}
