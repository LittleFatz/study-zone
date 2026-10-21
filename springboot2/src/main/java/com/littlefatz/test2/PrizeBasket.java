package com.littlefatz.test2;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PrizeBasket<T extends Fruit> {

    private String name;
    private List<T> fruits;

}
