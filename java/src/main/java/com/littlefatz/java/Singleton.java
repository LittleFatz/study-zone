package com.littlefatz.java;

public enum Singleton {
    INSTANCE(1);

    private int value;

    Singleton (int value) {
        this.value = value;
        System.out.println("singleton");
    }

    public static void main(String[] args) {
        Singleton ins1 = Singleton.INSTANCE;
        Singleton ins2 = Singleton.INSTANCE;
        System.out.println(ins1.value);
        System.out.println(ins1 == ins2);
    }
}
