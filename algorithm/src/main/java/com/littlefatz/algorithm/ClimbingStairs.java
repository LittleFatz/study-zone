package com.littlefatz.algorithm;


//https://leetcode-cn.com/problems/climbing-stairs/
//斐波那契数列    f（3） = f（1） + f（2）
//最近子问题


public class ClimbingStairs {

    public int climbStairs(int n) {
        if (n <= 2) {
            return n;
        }

        int f1 = 1;
        int f2 = 2;
        int f3 = 3;
        for (int i = 3; i <= n; i++) {
            f3 = f1 + f2;
            f1 = f2;
            f2 = f3;
        }

        return f3;
    }
}
