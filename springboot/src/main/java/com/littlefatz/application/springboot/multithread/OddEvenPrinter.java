package com.littlefatz.application.springboot.multithread;

public class OddEvenPrinter {

    private static Object lock = new Object();
    private static int maxPrintCount = 20;
    private static int count = 0;

    static class OddPrinter implements Runnable {
        @Override
        public void run() {
            while (true) {
                synchronized (lock) {
                    while (count % 2 == 0 || count > maxPrintCount) {
                        if (count > maxPrintCount) {
                            lock.notifyAll();
                            return;
                        }

                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }

                    System.out.println("OddTask print: " + count++);
                    lock.notifyAll();
                }
            }
        }
    }

    static class EvenPrinter implements Runnable {
        @Override
        public void run() {

            while (true) {

                synchronized (lock) {
                    while (count % 2 == 1 || count > maxPrintCount) {
                        if (count > maxPrintCount) {
                            lock.notifyAll();
                            return;
                        }

                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }

                    System.out.println("EvenTask print: " + count++);
                    lock.notifyAll();
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException {

        Thread oddThread = new Thread(new OddPrinter(), "oddThread");
        Thread evenThread = new Thread(new EvenPrinter(), "evenThread");

        oddThread.start();
        evenThread.start();
        oddThread.join();
        evenThread.join();

        System.out.println("Finish");


    }
}
