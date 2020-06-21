package com.littlefatz.algorithm;

/*
https://leetcode-cn.com/problems/move-zeroes/

双指针

[0,1,0,3,12]
 */


public class MoveZero {
    public void moveZeroes(int[] nums) {
        int j = 0;
        for (int i = 0; i < nums.length; i++) {
            if (nums[i] != 0) {
                if (i != j) {
                    nums[j] = nums[i];
                    nums[i] = 0;
                }
                j++;
            }
        }
    }

    public static void main(String[] args) {
        int[] a = new int[]{2,1};
        MoveZero test = new MoveZero();
        test.moveZeroes(a);
        for (int i = 0; i < a.length; i++) {
            System.out.println(a[i]);
        }
    }
}
