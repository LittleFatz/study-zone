package com.littlefatz.algorithm;

import java.util.HashMap;
import java.util.Map;

public class SynchronizedTest implements Runnable{
    private static int count = 0;
    private final Map<String, Integer> map = new HashMap<>();

    public Map<String, Integer> getMap() {
        return map;
    }

    public static void main(String[] args) throws InterruptedException {
        SynchronizedTest test = new SynchronizedTest();


        Thread t1 = new Thread(test, "thread1");
        Thread t2 = new Thread(test, "thread2");

        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println(test.getMap().get("data"));
        System.out.println("~~~~~~~~~~~~~end");

    }


    @Override
    public void run() {
//        for (int i = 0; i < 5; i++) {
//            count++;
//            System.out.println("current count:" + count);
//        }

        String threadName = Thread.currentThread().getName();
        if (threadName.equals("thread1")) {
            increase();
        } else if (threadName.equals("thread2")) {
            clear();
        }


    }

    private void increase() {
        synchronized (map) {
            for (int i = 0; i < 10; i++) {
                map.put("data", i);
                System.out.println(Thread.currentThread() + "put data:" + i);
            }
        }

    }

    private void clear() {
        synchronized (map) {
            for (int i = 0; i < 10; i++) {
                map.put("data", 0);
                System.out.println(Thread.currentThread() + "clear data:" + i);

            }
        }

    }



}





