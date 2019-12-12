package com.github.chenlijia1111.util.leetCode;

import java.util.*;

/**
 * 寻找两个有序数组的中位数
 * 给定两个大小为 m 和 n 的有序数组 nums1 和 nums2。
 * <p>
 * 请你找出这两个有序数组的中位数，并且要求算法的时间复杂度为 O(log(m + n))。
 * <p>
 * 你可以假设 nums1 和 nums2 不会同时为空。
 * <p>
 * 示例 1:
 * <p>
 * nums1 = [1, 3]
 * nums2 = [2]
 * <p>
 * 则中位数是 2.0
 * <p>
 * 示例 2:
 * <p>
 * nums1 = [1, 2]
 * nums2 = [3, 4]
 * <p>
 * 则中位数是 (2 + 3)/2 = 2.5
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/5 0005 上午 9:22
 **/
public class FindMedianSortedArrays {

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        List<Integer> list = new ArrayList<>();
        if (Objects.nonNull(nums1)) {
            for (int i = 0; i < nums1.length; i++) {
                list.add(nums1[i]);
            }
        }
        if (Objects.nonNull(nums2)) {
            for (int i = 0; i < nums2.length; i++) {
                list.add(nums2[i]);
            }
        }
        list.sort(Comparator.comparing(e -> e));
        if (list.size() > 0) {
            int size = list.size();
            int i = size % 2;
            int i1 = size / 2;
            if (i == 0) {
                //偶数个
                return (list.get(i1 -1 ) + list.get(i1)) / 2.0;
            } else {
                //奇数个
                return list.get(i1);
            }
        }
        return 0.0;
    }

}
