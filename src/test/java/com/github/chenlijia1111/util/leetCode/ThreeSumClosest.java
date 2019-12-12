package com.github.chenlijia1111.util.leetCode;

import org.junit.Test;

import java.util.Arrays;

/**
 * 最接近的三数之和
 * 给定一个包括 n 个整数的数组 nums 和 一个目标值 target。找出 nums 中的三个整数，使得它们的和与 target 最接近。返回这三个数的和。假定每组输入只存在唯一答案。
 * <p>
 * 例如，给定数组 nums = [-1，2，1，-4], 和 target = 1.
 * <p>
 * 与 target 最接近的三个数的和为 2. (-1 + 2 + 1 = 2).
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/6 0006 下午 5:19
 **/
public class ThreeSumClosest {


    public int threeSumClosest(int[] nums, int target) {
        Arrays.sort(nums);
        int minSum = Integer.MAX_VALUE;
        for (int i = 0; i < nums.length; i++) {
            int l = i + 1;
            int r = nums.length - 1;
            while (l < r) {
                int sum = nums[i] + nums[l] + nums[r];
                if (sum == target) {
                    l++;
                    r--;
                } else if (sum < target) {
                    l++;
                } else {
                    r--;
                }
                minSum = Math.abs(sum - target) < Math.abs(minSum == Integer.MAX_VALUE ? minSum : (minSum - target)) ? sum : minSum;
            }
        }
        return minSum;
    }


    @Test
    public void test() {
        int[] nums = {1, 1, -1, -1, 3};
        threeSumClosest(nums, -1);
    }

}
