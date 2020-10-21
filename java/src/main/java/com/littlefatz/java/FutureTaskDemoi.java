package com.littlefatz.java;

import java.util.concurrent.*;

public class FutureTaskDemoi {

    public void main() throws ExecutionException, InterruptedException {
        System.out.println("main start");
        Callable<Integer> task = new MyCallable(3);
        FutureTask<Integer> future = new FutureTask<Integer>(task);
        new Thread(future).start();

        System.out.println("run task");
        int sum = future.get();
        System.out.println(sum);
        System.out.println("task end");

        System.out.println("main end");

//        Thread thread = new Thread() {
//            @Override
//            public void run() {
//                System.out.println("main start");
//
//                try {
//                    int sum = future.get();
//
//                    System.out.println(sum);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//                System.out.println("main end");
//
//            }
//        };
//
//        thread.start();

    }

    class MyCallable implements Callable<Integer> {

        private int count;

        public MyCallable(int count) {
            this.count = count;
        }

        public Integer call() throws Exception {

            System.out.println("Task start");
            int i = 0;
            int sum = 0;
            while (i <= count) {
                sum += i;
                i++;
            }

            System.out.println("task end");
            return sum;


        }
    }

    public static void main(String[] args) {
        FutureTaskDemoi test = new FutureTaskDemoi();

        ExecutorService threadPool = Executors.newSingleThreadExecutor();


//        threadPool.submit()
//        threadPool.execute();

        try {
            test.main();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
