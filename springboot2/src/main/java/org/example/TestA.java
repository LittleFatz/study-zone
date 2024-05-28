package org.example;

public class TestA {

    public static void test() {
        TestB testB = new TestB();
        testB.test(new ChildImpl());
    }

    public static void foo() {
        System.out.println("in hello foo");
    }
}
