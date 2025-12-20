package com.littlefatz.application.springboot.controller;

import com.littlefatz.application.springboot.dto.ValidateRequest;
import com.littlefatz.application.springboot.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.redis.core.RedisCallback;
//import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    /**
     * 方式 1：使用 BindingResult 手动处理验证错误
     * 验证失败时不会抛异常，可以自定义错误响应格式
     */
    @RequestMapping(method = RequestMethod.POST, path = "/validate")
    @ResponseBody
    public String validate(@Valid @RequestBody ValidateRequest request, BindingResult bindingResult) {
        // 检查验证结果
        if (bindingResult.hasErrors()) {
            String errors = bindingResult.getAllErrors().stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(", "));
            return "验证失败: " + errors;
        }

        System.out.println("接收到验证请求: " + request);

        // 这里可以添加你的业务逻辑
        String name = request.getName();
        String info = request.getInfo();

        return "验证成功 - Name: " + name + ", Info: " + info;
    }

    /**
     * 方式 2：不使用 BindingResult，让 Spring 自动抛异常
     * 验证失败时会抛出 MethodArgumentNotValidException，返回 HTTP 400
     */
    @RequestMapping(method = RequestMethod.POST, path = "/validate-auto")
    @ResponseBody
    public String validateAuto(@Valid @RequestBody ValidateRequest request) {
        // 如果验证失败，请求不会到达这里
        // Spring 会自动抛异常并返回标准的 400 错误响应
        System.out.println("接收到验证请求: " + request);
        return "验证成功 - Name: " + request.getName() + ", Info: " + request.getInfo();
    }

    /**
     * 方式 3：实验 - 不使用 @Valid 注解
     * 看看会发生什么：验证注解会被忽略！
     */
    @RequestMapping(method = RequestMethod.POST, path = "/validate-without-valid")
    @ResponseBody
    public String validateWithoutValid(@RequestBody ValidateRequest request) {
        // ⚠️ 没有 @Valid，即使 info 不是 JSON 格式，也不会触发验证
        // 请求会正常执行到这里
        System.out.println("没有 @Valid 的请求: " + request);
        System.out.println("info 的值: " + request.getInfo());

        return "没有验证就成功了！Name: " + request.getName() +
               ", Info: " + request.getInfo() +
               " (注意：info 可能不是有效的 JSON，但没有验证)";
    }

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
