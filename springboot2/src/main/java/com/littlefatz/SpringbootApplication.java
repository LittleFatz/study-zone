package com.littlefatz;

import com.littlefatz.test.Deadlock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class SpringbootApplication implements ApplicationRunner {


    @Autowired
    private Deadlock deadlock;

    public static void main(String[] args) {
        SpringApplication.run(SpringbootApplication.class, args);
    }

    @Override
    public void run(ApplicationArguments args) {
        System.out.println(Thread.currentThread().getName() + ": hahahah");
//        asyncService.testAsync2();


        deadlock.testAsync();
        System.out.println(Thread.currentThread().getName() + ": hahahah - end");

    }



}
