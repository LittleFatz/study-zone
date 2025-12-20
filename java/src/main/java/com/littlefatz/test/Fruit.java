package com.littlefatz.test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fruit {

    public static void main(String[] args) {


        //一般情况
//        List<Apple> appleList = new ArrayList<>();
//        Apple apple1 = new Apple();
//        Apple apple2 = new Apple();
//        List<Fruit> fruitList = appleList;
//



        //不能写入
//        List<? extends Fruit> fruitList = new ArrayList<>();
//
//        Apple apple1 = new Apple();
//        Apple apple2 = new Apple();
//
//        fruitList.add(apple1);
//        fruitList.add(apple2);


        //可以读取
//        List<Apple> appleList = new ArrayList<>();
//        Apple apple1 = new Apple();
//        Apple apple2 = new Apple();
//
//        appleList.add(apple1);
//        appleList.add(apple2);
//        List<? extends Fruit> fruitList = appleList;
//        Fruit fruit = fruitList.get(0);


        //可以写入


        List<? super Apple> appleList = new ArrayList<>();
        GreenApple greenApple = new GreenApple();
        RedApple redApple = new RedApple();

        appleList.add(greenApple);
        appleList.add(redApple);

        Fruit fruit = new Fruit();

//        appleList.add(fruit);

    }
}
