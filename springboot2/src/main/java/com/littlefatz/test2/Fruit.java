package com.littlefatz.test2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 水果基类
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Fruit {
    private String name;
    private String color;
    private double price;
}
