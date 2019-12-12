package com.github.chenlijia1111.util.leetCode;

/**
 * 回文数
 * 判断一个整数是否是回文数。回文数是指正序（从左向右）和倒序（从右向左）读都是一样的整数。
 * <p>
 * 示例 1:
 * <p>
 * 输入: 121
 * 输出: true
 * <p>
 * 示例 2:
 * <p>
 * 输入: -121
 * 输出: false
 * 解释: 从左向右读, 为 -121 。 从右向左读, 为 121- 。因此它不是一个回文数。
 * <p>
 * 示例 3:
 * <p>
 * 输入: 10
 * 输出: false
 * 解释: 从右向左读, 为 01 。因此它不是一个回文数。
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/5 0005 下午 4:30
 **/
public class IsPalindrome {

    public boolean isPalindrome(int x) {
        if (x >= 0) {
            String s = String.valueOf(x);
            String s1 = new StringBuilder(s).reverse().toString();
            return s.equals(s1);
        }
        return false;
    }

}
