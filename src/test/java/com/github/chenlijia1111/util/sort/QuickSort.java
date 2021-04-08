package com.github.chenlijia1111.util.sort;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;

/**
 * 快速排序
 * 原理：选中一个基准值，分别从左边和右边进行扫描，最终让比基准值小的在左边，比基准值大的在右边
 * 然后分别对左边和右边的数据进行递归
 *
 * @author Chen LiJia
 * @since 2021/3/16
 */
public class QuickSort {

    int reCount = 0;


    public int[] quickSort(int[] array, int low, int height) {
        if (array.length > 1 && height > low) {
            int copyHeight = height;
            int copyLow = low;
            // 进行处理
            // 选定基准值，第一个定位基准值
            int temp = array[low];
            while (height > low) {
                // 先从右边开始
                while (height > low) {
                    if (array[height] < temp) {
                        // 小于基准值，把他放到左边去
                        array[low] = array[height];
                        break;
                    }
                    height--;
                }
                // 再从左边开始找
                while (height > low) {
                    if (array[low] > temp) {
                        // 把他放到右边
                        array[height] = array[low];
                        break;
                    }
                    low++;
                }

                if (height == low) {
                    array[height] = temp;
                }
            }

            reCount++;

            // 进行递归
            if (height - 1 > copyLow) {
                quickSort(array, copyLow, height - 1);
            }
            if (copyHeight > height + 1) {
                quickSort(array, height + 1, copyHeight);
            }

        }
        return array;
    }

    @Test
    public void test1() {
        int[] array = new int[100];
        Random random = new Random();
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(50);
        }
        System.out.println(Arrays.toString(array));
        quickSort(array, 0, array.length - 1);
        System.out.println(Arrays.toString(array));
        System.out.println("总递归次数" + reCount);
    }



}
