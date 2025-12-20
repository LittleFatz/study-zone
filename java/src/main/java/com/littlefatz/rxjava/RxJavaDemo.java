package com.littlefatz.rxjava;

import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class RxJavaDemo {

    public static void main(String[] args) {
        // 1. 创建一个数据源（Observable）
        Flowable<String> dataSource = Flowable.just("apple", "banana", "orange", "grape", "pineapple");

        // 2. 订阅并处理数据
        dataSource
                .observeOn(Schedulers.io()) // 切换到IO线程
                .map(String::toUpperCase)    // 将每个字符串转换为大写
                .doOnNext(item -> System.out.println("Processing on thread: " + Thread.currentThread().getName()))
                .observeOn(Schedulers.computation()) // 切换到计算线程
                .map(item -> item + " processed")    // 添加处理后的标记
                .doOnNext(item -> System.out.println("Processing on computation thread: " + Thread.currentThread().getName()))
                .observeOn(Schedulers.single()) // 切换到单一线程
                .subscribe(
                        item -> System.out.println("Received: " + item + " on thread: " + Thread.currentThread().getName()),
                        Throwable::printStackTrace, // 错误处理
                        () -> System.out.println("All items processed!")
                );

        // 3. 为了演示让主线程睡眠一段时间，等待异步任务完成
        try {
            Thread.sleep(3000); // 等待所有异步操作完成
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
