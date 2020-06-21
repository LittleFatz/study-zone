package com.littlefatz.algorithm;


//https://leetcode-cn.com/problems/3sum/
//先排序，再左右夹逼


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThreeSum {
    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> results = new ArrayList<List<Integer>>();

        Arrays.sort(nums);

        for (int k = 0; k < nums.length - 2; k++) {
            if (k > 0 && nums[k] == nums[k-1]) {
                continue;
            }
            if (nums[k] > 0) {
                return results;
            }
            for (int left = k + 1, right = nums.length - 1; left < right;) {
                int sum  = nums[k] + nums[left] + nums[right];
                if (sum == 0) {
                    results.add(Arrays.asList(nums[k], nums[left], nums[right]));
                    while (left < right && nums[left] == nums[left+1]) {
                        left++;
                    }
                    while (left < right && nums[right] == nums[right-1]) {
                        right--;
                    }
                    left++;
                    right--;

                } else if (sum < 0) {
                    left++;
                } else {
                    right--;
                }

            }

        }
        return results;

    }

    public static void main(String[] args) {
        int[] a = new int[]{-1,0,1,2,-1,-4};
        ThreeSum test = new ThreeSum();
        System.out.println(test.threeSum(a));


    }
}
