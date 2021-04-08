package com.github.chenlijia1111.util.sort;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 测试冒泡排序
 * 优化版
 *
 * @author Chen LiJia
 * @since 2021/2/23
 */
public class BubbleSort {

    /**
     * 冒泡排序
     *
     * @param array
     * @return
     */
    public int[] bubbleSort(int[] array) {
        if (null == array || array.length == 0) {
            return array;
        }
        int arrayLength = array.length;
        // 记录每次大循环最后一次交换的下标，意味着后面的数据都是不需要比较的，那么下一次大循环的时候，直接就到那个下标就可以了
        int lastSwapIndex = arrayLength - 1;
        // 进行 arrayLength - 1 次外循环就行了，每循环一次会找出第 i 大的数据放到右边
        for (int i = 0; i < arrayLength - 1; i++) {
            // 判断这次有没有交换过数据，如果整一轮都没有交换过数据，说明这后面的数据都是有序的，不需要继续比较了。
            boolean swapStatus = false;
            // 理论上来说，进行了 i 次外循环之后，就找出了 i 次第 i 大的放在右边，右边的都是不用比较的。
            int loopLength = arrayLength - 1 - i;
            // 判断一下 lastSwapIndex 和 loopLength 最小的那个
            loopLength = Math.min(loopLength, lastSwapIndex);
            for (int j = 0; j < loopLength; j++) {
                if (array[j] > array[j + 1]) {
                    // 交换位置
                    int temp = array[j];
                    array[j] = array[j + 1];
                    array[j + 1] = temp;
                    swapStatus = true;
                    lastSwapIndex = j;
                }
            }
            // 这一个大循环都没有交换过数据，所以不用执行下一次大循环了
            if (!swapStatus) {
                break;
            }
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
        bubbleSort(array);
        System.out.println(Arrays.toString(array));
    }

}
