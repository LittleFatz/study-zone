package com.littlefatz.virtualthread;

import java.util.Random;

/**
 * 模拟调用不同的下游系统。
 * 用 Thread.sleep 模拟网络 IO 阻塞，虚拟线程在等待 IO 时会让出载体线程。
 */
public class DownstreamClient {

    private static final Random RANDOM = new Random();

    /** 订单库（MySQL），正常 300ms，偶发抖动到 800ms */
    public String callOrderDatabase(long orderId) throws InterruptedException {
        sleep(300 + RANDOM.nextInt(500));
        return "订单库: order_" + orderId + " 落库成功";
    }

    /** 库存服务（Dubbo RPC），正常 200ms */
    public String callStockRpc(long orderId) throws InterruptedException {
        sleep(200 + RANDOM.nextInt(100));
        return "库存服务: order_" + orderId + " 扣减库存成功";
    }

    /** 物流网关（HTTP 第三方），最慢，400ms 起步 */
    public String callLogisticsHttp(long orderId) throws InterruptedException {
        sleep(400 + RANDOM.nextInt(300));
        return "物流网关: order_" + orderId + " 已创建运单";
    }

    /** 优惠券服务（Redis 缓存），最快，100ms */
    public String callCouponCache(long orderId) throws InterruptedException {
        sleep(100 + RANDOM.nextInt(100));
        return "优惠券服务: order_" + orderId + " 核销成功";
    }

    private void sleep(long millis) throws InterruptedException {
        // Thread.sleep 会让虚拟线程从载体线程上卸载（unmount），真正体现“用同步写法获得异步性能”
        Thread.sleep(millis);
    }
}
