package com.littlefatz.application.java;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ExecutorTest {

    public static void main(String[] args) {
        ThreadPoolExecutor executorService = new ThreadPoolExecutor(2, 2,
                30, TimeUnit.SECONDS, new ArrayBlockingQueue<>(10));
        Future future = executorService.submit(() -> {
            try {
                sayHi("submit");
            } catch (Exception e) {
                System.out.println("sayHi Exception");
                e.printStackTrace();
            }
        });

        try {
            future.get();
        } catch (Exception e) {
            System.out.println("future.get Exception");
            e.printStackTrace();
        }
    }

    private static void sayHi(String name) throws RuntimeException {
        String printStr = "【thread-name:" + Thread.currentThread().getName() + ",执行方式:" + name + "】";
        System.out.println(printStr);
        throw new RuntimeException(printStr + ",我异常啦!哈哈哈!");
    }
}
