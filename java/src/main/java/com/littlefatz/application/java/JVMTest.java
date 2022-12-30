package com.littlefatz.application.java;

import java.util.ArrayList;
import java.util.List;

public class JVMTest {

    private List<Integer> list = new ArrayList<>();

    public void add() {
        boolean flag = true;
        while (flag) {
            list.add(1);
//            System.out.println("hello");
        }

    }

    public static void main(String[] args) {
        JVMTest test = new JVMTest();
        test.add();

    }
}
