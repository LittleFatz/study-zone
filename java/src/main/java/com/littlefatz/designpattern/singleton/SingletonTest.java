package com.littlefatz.designpattern.singleton;

public class SingletonTest {
    private static volatile SingletonTest instance;

    private SingletonTest () {
        if (instance == null) {
            throw new IllegalStateException("单例对象不允许通过反射重复创建");
        }
    }

    public static SingletonTest getInstance() {
        if (instance == null) {
            synchronized (SingletonTest.class) {
                if (instance == null) {
                    instance = new SingletonTest();
                }
            }
        }

        return instance;
    }
}
