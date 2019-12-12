package com.github.chenlijia1111.util.leetCode;

import org.junit.Test;

/**
 * 整数反转
 * 给出一个 32 位的有符号整数，你需要将这个整数中每位上的数字进行反转。
 * <p>
 * 示例 1:
 * <p>
 * 输入: 123
 * 输出: 321
 * <p>
 * 示例 2:
 * <p>
 * 输入: -123
 * 输出: -321
 * <p>
 * 示例 3:
 * <p>
 * 输入: 120
 * 输出: 21
 * <p>
 * 注意:
 * <p>
 * 假设我们的环境只能存储得下 32 位的有符号整数，则其数值范围为 [−2^31,  2^31 − 1]。请根据这个假设，如果反转后整数溢出那么就返回 0。
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/5 0005 下午 2:31
 **/
public class Reverse {

    public int reverse(int x) {
        //是否是正数
        boolean positive = x >= 0;
        String s = String.valueOf(x);
        if (s.contains("-")) {
            s = s.substring(1);
        }
        s = new StringBuilder(s).reverse().toString();
        Long l = Long.parseLong((positive ? "" : "-") + s);
        if (l <= Integer.MAX_VALUE && l >= Integer.MIN_VALUE) {
            return l.intValue();
        }
        return 0;
    }

    @Test
    public void test1() {
        System.out.println(reverse(1534236469));
        //1534236469
        //2147483647
    }

}
