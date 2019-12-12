package com.github.chenlijia1111.util.leetCode;

import org.junit.Test;

/**
 * Z字形变换
 * 将一个给定字符串根据给定的行数，以从上往下、从左到右进行 Z 字形排列。
 * <p>
 * 比如输入字符串为 "LEETCODEISHIRING" 行数为 3 时，排列如下：
 * <p>
 * L   C   I   R
 * E T O E S I I G
 * E   D   H   N
 * <p>
 * 之后，你的输出需要从左往右逐行读取，产生出一个新的字符串，比如："LCIRETOESIIGEDHN"。
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/5 0005 下午 1:34
 **/
public class Convert {


    public String convert(String s, int numRows) {
        if (numRows == 1) {
            return s;
        }
        int length = s.length();
        char[][] arrays = new char[numRows][length];
        int preXIndex = 0;
        int preYIndex = 0;
        for (int i = 0; i < length; i++) {
            char c = s.charAt(i);
            arrays[preXIndex][preYIndex] = c;
            int i1 = preYIndex % (numRows - 1);
            if (i1 == 0) {
                //说明是纵向列
                if (preXIndex == (numRows - 1)) {
                    //触底了,需要斜
                    preYIndex += 1;
                    preXIndex -= 1;
                } else {
                    //接着往下
                    preXIndex += 1;
                }
            } else {
                //说明是斜向列
                preYIndex += 1;
                preXIndex -= 1;
            }
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < arrays.length; i++) {
            char[] array = arrays[i];
            for (int j = 0; j < array.length; j++) {
                if (array[j] != 0) {
                    sb.append(array[j]);
                }
            }
        }
        return sb.toString();
    }

    @Test
    public void test() {
        String s = convert("A", 1);
        System.out.println(s);
    }

}
