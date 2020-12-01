package com.littlefatz.java;

import com.sun.org.apache.regexp.internal.RE;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

//多线程按顺序调用，A->B->C，AA 打印 5 次，BB 打印10 次，CC 打印 15 次，重复 10 次
public class MultipleABCPrinter {

    private ReentrantLock lock = new ReentrantLock();
    private Condition printA = lock.newCondition();
    private Condition printB = lock.newCondition();
    private Condition printC = lock.newCondition();
    private int repeat = 0;
    private int state = 0;

    public MultipleABCPrinter(int repeat) {
        this.repeat = repeat;
    }

    public void print(String letter, int times, int targetState, Condition current, Condition next) {
        for (int i = 0; i < repeat;) {
            lock.lock();
            try {
                if (state % 3 != targetState) {
                    current.await();
                }

                state++;
                i++;
                for (int j = 0; j < times; j++) {
                    System.out.println(letter);
                }
                next.signal();

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }

        }
    }

    public static void main(String[] args) throws InterruptedException {
        final MultipleABCPrinter test = new MultipleABCPrinter(10);
        Thread thread1 = new Thread(() -> {
            test.print("AA", 5, 0, test.printA, test.printB);
        });

        Thread thread2 = new Thread(() -> {
            test.print("BB", 10, 1, test.printB, test.printC);
        });

        Thread thread3 = new Thread(() -> {
            test.print("CC", 15, 2, test.printC, test.printA);
        });

        thread1.start();
        thread2.start();
        thread3.start();

        thread1.join();
        thread2.join();
        thread3.join();
    }
}
