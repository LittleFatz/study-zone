package com.littlefatz.virtualthread;

import java.util.List;
import java.util.concurrent.Callable;

/**
 * 单个订单的处理任务：依次调用 4 个不同的下游系统并汇总结果。
 * 每个任务跑在一个独立的虚拟线程里。
 */
public class OrderProcessTask implements Callable<OrderProcessTask.OrderResult> {

    private final long orderId;
    private final DownstreamClient downstreamClient;

    public OrderProcessTask(long orderId, DownstreamClient downstreamClient) {
        this.orderId = orderId;
        this.downstreamClient = downstreamClient;
    }

    @Override
    public OrderResult call() throws Exception {
        long start = System.currentTimeMillis();

        // 每个订单调用不同的下游系统（DB / RPC / HTTP / 缓存）
        String dbResult = downstreamClient.callOrderDatabase(orderId);
        String stockResult = downstreamClient.callStockRpc(orderId);
        String logisticsResult = downstreamClient.callLogisticsHttp(orderId);
        String couponResult = downstreamClient.callCouponCache(orderId);

        long elapsed = System.currentTimeMillis() - start;
        String threadName = Thread.currentThread().toString();
        return new OrderResult(orderId, List.of(dbResult, stockResult, logisticsResult, couponResult), elapsed, threadName);
    }

    public record OrderResult(long orderId, List<String> downstreamResults, long elapsedMillis, String threadName) {
    }
}
