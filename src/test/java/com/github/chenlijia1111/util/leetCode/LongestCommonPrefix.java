package com.github.chenlijia1111.util.leetCode;

import org.junit.Test;

/**
 * 编写一个函数来查找字符串数组中的最长公共前缀。
 * <p>
 * 如果不存在公共前缀，返回空字符串 ""。
 * <p>
 * 示例 1:
 * <p>
 * 输入: ["flower","flow","flight"]
 * 输出: "fl"
 * <p>
 * 示例 2:
 * <p>
 * 输入: ["dog","racecar","car"]
 * 输出: ""
 * 解释: 输入不存在公共前缀。
 * <p>
 * 说明:
 * <p>
 * 所有输入只包含小写字母 a-z 。
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/6 0006 下午 1:59
 **/
public class LongestCommonPrefix {

    public String longestCommonPrefixV1(String[] strs) {
        StringBuilder sb = new StringBuilder();
        if (strs != null && strs.length > 0) {
            if (strs.length == 1) {
                return strs[0];
            }
            int currentIndex = 0;
            String str = strs[0];
            while (true) {
                if (currentIndex >= str.length()) {
                    break;
                }
                char c = str.charAt(currentIndex);
                for (int i = 1; i < strs.length; i++) {
                    if (currentIndex >= strs[i].length() || c != strs[i].charAt(currentIndex)) {
                        return sb.toString();
                    }
                }
                sb.append(c);
                currentIndex++;
            }
        }
        return sb.toString();
    }

    public String longestCommonPrefixV2(String[] strs) {
        if (strs != null && strs.length > 0) {
            if (strs.length == 1) {
                return strs[0];
            }
            String prefix = strs[0];
            for (int i = 1; i < strs.length; i++) {
                while (strs[i].indexOf(prefix) != 0 && prefix.length() > 0){
                    prefix = prefix.substring(0,prefix.length() -1);
                }
            }
            return prefix;
        }
        return "";
    }

    @Test
    public void test(){
        String[] strs = {"flower","flow","flight"};
        String s = longestCommonPrefixV1(strs);
        System.out.println(s);
    }

}
