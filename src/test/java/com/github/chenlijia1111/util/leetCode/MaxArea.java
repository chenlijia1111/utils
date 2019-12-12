package com.github.chenlijia1111.util.leetCode;

/**
 * 盛最多水的容器
 * 给定 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0)。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
 * <p>
 * 说明：你不能倾斜容器，且 n 的值至少为 2。
 * <p>
 * 图中垂直线代表输入数组 [1,8,6,2,5,4,8,3,7]。在此情况下，容器能够容纳水（表示为蓝色部分）的最大值为 49。
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/5 0005 下午 4:46
 **/
public class MaxArea {

    public int maxAreaV1(int[] height) {
        int length = height.length;
        int max = 0;
        for (int i = 0; i < length; i++) {
            int iValue = height[i];
            for (int j = 0; j < length; j++) {
                if (i != j) {
                    int jValue = height[j];
                    int min = Math.min(iValue, jValue);
                    int i1 = Math.abs(j - i) * min;
                    max = Math.max(max, i1);
                }
            }
        }
        return max;
    }

    public int maxAreaV2(int[] height) {
        int length = height.length;
        int max = 0;
        for (int i = 0; i < length; i++) {
            int iValue = height[i];
            for (int j = i + 1; j < length; j++) {
                int jValue = height[j];
                int min = Math.min(iValue, jValue);
                int i1 = Math.abs(j - i) * min;
                max = Math.max(max, i1);
            }
        }
        return max;
    }

    public int maxAreaV3(int[] height) {
        int length = height.length;
        int max = 0;
        int left = 0;
        int right = length - 1;

        while (right >= left) {
            max = Math.max(max,Math.min(height[left], height[right]) * (right - left));
            if (height[left] <= height[right]) {
                left ++;
            } else {
                right --;
            }
        }

        return max;
    }

}
