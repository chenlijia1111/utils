package com.github.chenlijia1111.util.leetCode;

import org.junit.Test;

import java.util.Objects;

/**
 * 最长回文子串
 * <p>
 * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。
 * <p>
 * 示例 1：
 * <p>
 * 输入: "babad"
 * 输出: "bab"
 * 注意: "aba" 也是一个有效答案。
 * <p>
 * 示例 2：
 * <p>
 * 输入: "cbbd"
 * 输出: "bb"
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/5 0005 上午 10:19
 **/
public class LongestPalindrome {


    public String longestPalindromeV1(String s) {
        if (null != s && s.length() > 0) {
            char[] chars = s.toCharArray();
            String maxStr = ""; //最大回文
            //滑动区间[i-j]
            for (int i = 0; i < chars.length; i++) {
                for (int j = i; j < chars.length; j++) {
                    String substring = s.substring(i, j + 1);
                    if (check(substring)) {
                        maxStr = maxStr.length() >= substring.length() ? maxStr : substring;
                    }
                }
            }
            return maxStr;

        }
        return "";
    }

    //校验字符串是否是回文
    private boolean check(String s) {
        if (null != s) {
            int length = s.length();
            int i = length / 2;
            if (length % 2 == 0) {
                //偶数
                String left = s.substring(0, i);
                String right = s.substring(i, length);
                return Objects.equals(left, new StringBuilder(right).reverse().toString());
            } else {
                //奇数
                String left = s.substring(0, i);
                String right = s.substring(i + 1, length);
                return Objects.equals(left, new StringBuilder(right).reverse().toString());
            }
        }
        return false;
    }


    public String longestPalindromeV2(String s) {
        String maxStr = "";
        if(null != s && s.length() > 0){
            maxStr = String.valueOf(s.charAt(0));
        }
        if (null != s && s.length() > 1) {
            int length = s.length();
            int loopSize = length * 2 - 1;
            for (int i = 0; i < loopSize; i++) {
                //奇数就是中间值
                //偶数就是真实值
                int i1 = i % 2;

                int left = 0;
                int right = 0;
                if (i1 == 0) {
                    //偶数
                    left = i - 2;
                    right = i + 2;

                } else {
                    //奇数
                    left = i - 1;
                    right = i + 1;
                }
                while (left >= 0 && right < loopSize) {
                    if (s.charAt(left / 2) == s.charAt(right / 2)) {
                        //相等
                        String substring = s.substring(left / 2, right / 2 + 1);
                        maxStr = maxStr.length() > substring.length() ? maxStr : substring;
                        left -= 2;
                        right += 2;
                    }else {
                        break;
                    }
                }
            }

        }else {
            return s;
        }
        return maxStr;
    }


    @Test
    public void test() {
        System.out.println(longestPalindromeV2("ac"));

    }

}
