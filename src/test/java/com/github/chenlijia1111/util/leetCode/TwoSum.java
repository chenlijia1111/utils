package com.github.chenlijia1111.util.leetCode;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author 陈礼佳
 * @version 1.0
 * @since 2019/12/4 0004 下午 2:04
 **/
public class TwoSum {

    /**
     * 两数之和
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
     * 给定 nums = [2, 7, 11, 15], target = 9
     * <p>
     * 因为 nums[0] + nums[1] = 2 + 7 = 9
     * 所以返回 [0, 1]
     *
     * @param nums   1
     * @param target 2
     * @return int[]
     * @since 下午 2:05 2019/12/4 0004
     **/
    public int[] twoSumV1(int[] nums, int target) {
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            for (int j = 0; j < nums.length; j++) {
                if (i != j && target - num == nums[j]) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    /**
     * 两数之和
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
     * 给定 nums = [2, 7, 11, 15], target = 9
     * <p>
     * 因为 nums[0] + nums[1] = 2 + 7 = 9
     * 所以返回 [0, 1]
     *
     * @param nums   1
     * @param target 2
     * @return int[]
     * @since 下午 2:05 2019/12/4 0004
     **/
    public int[] twoSumV2(int[] nums, int target) {
        //用来存记录--值-下标
        HashMap<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            int num = nums[i];
            //要查找的值
            int otherNum = target - num;
            boolean b = map.containsKey(otherNum);
            if (b) {
                //查找这个值的下标
                Integer otherNumIndex = map.get(otherNum);
                return new int[]{Math.min(i, otherNumIndex), Math.max(otherNumIndex, i)};
            }
            //找不到,后面赋值到map,保证不会由于值相同在取值的时候覆盖之前的值的下标
            map.put(num, i);
        }
        return null;
    }

    @Test
    public void test1() {
        int[] array = {3, 3};
        int[] ints = twoSumV2(array, 6);
        System.out.println(Arrays.toString(ints));
    }
}
