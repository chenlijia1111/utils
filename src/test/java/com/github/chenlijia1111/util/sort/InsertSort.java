package com.github.chenlijia1111.util.sort;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * 插入排序
 *
 * @author Chen LiJia
 * @since 2021/2/23
 */
public class InsertSort {

    /**
     * 插入排序
     * 默认左边的是排好序的，然后将右边的数据插入到左边适当的位置
     *
     * @param array
     * @return
     */
    public int[] insertSort(int[] array) {
        if (null == array || array.length == 0) {
            return array;
        }
        // 当前用来比较的值，从下标 0 开始循环
        int current;
        int length = array.length;
        for (int i = 0; i < length - 1; i++) {
            // 定义当前比较值
            current = array[i + 1];
            // 对左边的数据进行比较，如果比左边的小的话，直接让那个值往右移一位
            int preIndex = i;
            while (preIndex >= 0 && current < array[preIndex]) {
                array[preIndex + 1] = array[preIndex];
                preIndex--;
            }
            // 现在这个 preIndex 的值是小于等于当前值的
            // 插入到适当的位置
            array[preIndex + 1] = current;
        }
        return array;
    }

    /**
     * 插入排序
     * 默认左边的是排好序的，然后将右边的数据插入到左边适当的位置
     *
     * 优化，优化的时机是，当往左查询插入位置的时候，是一个一个线性查找的，这个就会比较慢
     * 因为前面都是排好序的，所以这里可以通过二分查找来确定位置，确定了位置之后，这个位置后面的数据都后移一位，
     * 然后插入到那个位置
     *
     * @param array
     * @return
     */
    public int[] insertSort2(int[] array) {
        if (null == array || array.length == 0) {
            return array;
        }
        // 当前用来比较的值，从下标 0 开始循环
        int current;
        int length = array.length;
        for (int i = 0; i < length - 1; i++) {
            // 定义当前比较值
            current = array[i + 1];
            // 对左边的数据进行比较，如果比左边的小的话，直接让那个值往右移一位
            int preIndex = i;
            while (preIndex >= 0 && current < array[preIndex]) {
                array[preIndex + 1] = array[preIndex];
                preIndex--;
            }
            // 现在这个 preIndex 的值是小于等于当前值的
            // 插入到适当的位置
            array[preIndex + 1] = current;
        }
        return array;
    }


    @Test
    public void test1() {
        int[] array = new int[20];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(50);
        }
        System.out.println(Arrays.toString(array));
        insertSort(array);
        System.out.println(Arrays.toString(array));
    }
}
