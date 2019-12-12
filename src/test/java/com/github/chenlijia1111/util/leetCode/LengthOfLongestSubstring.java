package com.github.chenlijia1111.util.leetCode;

import org.junit.Test;

import java.util.*;

/**
 * 给定一个字符串，请你找出其中不含有重复字符的 最长子串 的长度。
 * <p>
 * 示例 1:
 * <p>
 * 输入: "abcabcbb"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "abc"，所以其长度为 3。
 * <p>
 * 示例 2:
 * <p>
 * 输入: "bbbbb"
 * 输出: 1
 * 解释: 因为无重复字符的最长子串是 "b"，所以其长度为 1。
 * <p>
 * 示例 3:
 * <p>
 * 输入: "pwwkew"
 * 输出: 3
 * 解释: 因为无重复字符的最长子串是 "wke"，所以其长度为 3。
 * 请注意，你的答案必须是 子串 的长度，"pwke" 是一个子序列，不是子串。
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/4 0004 下午 3:06
 **/
public class LengthOfLongestSubstring {

    public int lengthOfLongestSubstringV1(String s) {
        List<StringBuilder> sbList = new ArrayList<>();
        char[] chars = s.toCharArray();
        StringBuilder sb = new StringBuilder();
        sbList.add(sb);
        for (int i = 0; i < chars.length; i++) {
            //判断是否重复
            int index = sb.indexOf(String.valueOf(chars[i]));
            if (index != -1) {
                //重新定位到这个字符上次出现的位置+1
                i = s.lastIndexOf(chars[i], i - 1) + 1;
                //存在相同的值,创建一个新的StringBuilder
                sb = new StringBuilder();
                sbList.add(sb);
            }
            sb.append(chars[i]);
        }
        //查找长度最长的
        System.out.println(sbList);
        Integer maxLength = sbList.stream().map(e -> e.length()).max(Comparator.comparing(e -> e)).get();
        return maxLength;
    }

    //滑动窗口的形式
    public int lengthOfLongestSubstringV2(String s) {
        char[] chars = s.toCharArray();
        Set<Character> characters = new HashSet<>();
        int maxLength = 0;
        int start = 0; //窗口区间
        int end = 0; //窗口区间
        for (int i = 0; i < chars.length; i++) {
            //判断是否重复
            char aChar = chars[i];
            if (characters.contains(aChar)) {
                maxLength = Math.max(maxLength, end - start);
                //包含了
                //开始区间需要调到上一次出现的下一个位置
                int lastIndexOf = s.lastIndexOf(aChar, i - 1);
                start = lastIndexOf + 1;
                end = start;
                i = start - 1;
                characters.clear();
                //判断一下长度,是否
            } else {
                characters.add(aChar);
                end += 1;
            }

        }
        //查找长度最长的
        return Math.max(maxLength, end - start);
    }

    //滑动窗口的形式
    public int lengthOfLongestSubstringV3(String s) {
        int n = s.length(), ans = 0;

        //值,下标值
        Map<Character, Integer> map = new HashMap<>();

        for (int j = 0, i = 0; j < n; j++) {
            if (map.containsKey(s.charAt(j))) {
                i = Math.max(map.get(s.charAt(j)) + 1, i);
            }
            ans = Math.max(ans, j - i + 1);
            map.put(s.charAt(j), j);
        }
        return ans;
    }

    @Test
    public void test1() {
        String s = "abba";
        System.out.println(lengthOfLongestSubstringV3(s));
    }

}
