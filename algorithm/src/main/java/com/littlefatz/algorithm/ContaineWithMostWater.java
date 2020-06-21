package com.littlefatz.algorithm;

//https://leetcode-cn.com/problems/container-with-most-water
//左右夹逼


public class ContaineWithMostWater {
    public int maxArea(int[] height) {
        int maxArea = 0;
        for (int i = 0, j = height.length -1; i != j; ) {
            int minBar = height[i] < height[j] ? height[i++] : height[j--];
            int tempArea = minBar * (j - i + 1);
            maxArea = Math.max(tempArea, maxArea);
        }

        return maxArea;
    }

    public static void main(String[] args) {
        int[] a = new int[]{1,8,6,2,5,4,8,3,7};
        ContaineWithMostWater test = new ContaineWithMostWater();
        System.out.println(test.maxArea(a));


    }
}
