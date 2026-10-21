package com.littlefatz.test;

import java.util.concurrent.locks.ReentrantLock;

public class TestA {

    private static final ReentrantLock lock = new ReentrantLock();

    public void test() {

        lock.lock();

        try {
            System.out.println("执行业务逻辑，当前线程: " + Thread.currentThread().getName());
        } finally {
            lock.unlock();
        }
    }

    public void foo() {
        System.out.println("in hello foo");
    }
}
