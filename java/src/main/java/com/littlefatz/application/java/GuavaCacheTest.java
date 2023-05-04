package com.littlefatz.application.java;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;

import java.util.concurrent.*;

public class GuavaCacheTest {



    ListeningExecutorService backgroundRefreshPools = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(3));


    private  LoadingCache<String, String> localCache = CacheBuilder.newBuilder()
            .maximumSize(32)
            .refreshAfterWrite(3, TimeUnit.SECONDS)
            .expireAfterWrite(30, TimeUnit.SECONDS)
            .build(new CacheLoader<String, String>() {

                @Override
                public String load(String s) throws Exception {
                    System.out.println(Thread.currentThread().getName() + " loading key");
                    return s + "_cache";
                }

                @Override
                public ListenableFuture<String> reload(String key,
                                                       String oldValue) throws Exception {
                    return backgroundRefreshPools.submit(new Callable<String>() {

                        @Override
                        public String call() throws Exception {
                            System.out.println(Thread.currentThread().getName() + "trigger reload");
                            return "reloadKey" + ThreadLocalRandom.current().nextInt(100);
                        }
                    });
                }

            });

    public void test() {

        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        String value = localCache.get("test1");
                        System.out.println(Thread.currentThread().getName() + "-" + value);
                        Thread.sleep(1000L);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        });
        thread1.start();


//        Thread thread2 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                while (true) {
//                    try {
//                        String value = localCache.get("test1");
//                        System.out.println(Thread.currentThread().getName() + value);
//                        Thread.sleep(2000L);
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//
//                }
//            }
//        });
//        thread2.start();
    }

    public static void main(String[] args) {
        GuavaCacheTest test = new GuavaCacheTest();
        test.test();
    }
}
