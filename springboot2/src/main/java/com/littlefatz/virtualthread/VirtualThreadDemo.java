package com.littlefatz.virtualthread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 虚拟线程并行处理订单 demo：
 * 每个订单一个虚拟线程，各自调用不同的下游系统，最后汇总结果。
 * 同时用平台线程池跑一遍做耗时对比。
 */
public class VirtualThreadDemo {

    private static final int ORDER_COUNT = 20;

    public static void main(String[] args) throws Exception {
        DownstreamClient client = new DownstreamClient();
        List<Callable<OrderProcessTask.OrderResult>> tasks = new ArrayList<>();
        for (long i = 1; i <= ORDER_COUNT; i++) {
            tasks.add(new OrderProcessTask(i, client));
        }

        System.out.println("========== 虚拟线程版本 ==========");
        long virtualElapsed = runWithExecutor(tasks, Thread.ofVirtual().factory());

        System.out.println("\n========== 平台线程池版本（8 线程）==========");
        AtomicInteger platformIndex = new AtomicInteger();
        ThreadFactory platformFactory = r -> new Thread(r, "platform-worker-" + platformIndex.incrementAndGet());
        long platformElapsed = runWithExecutor(tasks, platformFactory, 8);

        System.out.println("\n========== 对比汇总 ==========");
        System.out.printf("订单数: %d, 每单调用 4 个下游系统（sleep 模拟 IO）%n", ORDER_COUNT);
        System.out.printf("虚拟线程总耗时: %d ms%n", virtualElapsed);
        System.out.printf("平台线程池总耗时: %d ms%n", platformElapsed);
    }

    private static long runWithExecutor(List<Callable<OrderProcessTask.OrderResult>> tasks,
                                        ThreadFactory threadFactory) throws Exception {
        return runWithExecutor(tasks, threadFactory, 0);
    }

    private static long runWithExecutor(List<Callable<OrderProcessTask.OrderResult>> tasks,
                                        ThreadFactory threadFactory, int poolSize) throws Exception {
        // poolSize > 0 时用固定平台线程池；否则每个任务一个新虚拟线程
        ExecutorService executor = poolSize > 0
                ? Executors.newFixedThreadPool(poolSize, threadFactory)
                : Executors.newThreadPerTaskExecutor(threadFactory);

        long start = System.currentTimeMillis();
        try {
            List<CompletableFuture<OrderProcessTask.OrderResult>> futures = tasks.stream()
                    .map(task -> CompletableFuture.supplyAsync(() -> {
                        try {
                            return task.call();
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }, executor))
                    .toList();

            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

            for (CompletableFuture<OrderProcessTask.OrderResult> future : futures) {
                OrderProcessTask.OrderResult result = future.join();
                System.out.printf("[%s] 订单 %d 处理完成, 耗时 %d ms, 下游返回: %s%n",
                        result.threadName(), result.orderId(), result.elapsedMillis(),
                        result.downstreamResults());
            }
        } finally {
            executor.shutdown();
        }
        return System.currentTimeMillis() - start;
    }
}
