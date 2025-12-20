package com.littlefatz.rxjava;


import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.util.function.Consumer;

public class WebFluxTest {

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();
        Flux.just("hello", "world")
                .publishOn(Schedulers.single())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String t) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                        }
                        System.out.println(Thread.currentThread().getName() + " " + t);
                    }

                }, Throwable::printStackTrace);
        System.out.println("cost:" + (System.currentTimeMillis() - start));
        Thread.currentThread().join();
    }
}
