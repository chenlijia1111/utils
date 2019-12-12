package com.github.chenlijia1111.util.leetCode;

/**
 * 罗马数字包含以下七种字符： I， V， X， L，C，D 和 M。
 * <p>
 * 字符          数值
 * I             1
 * V             5
 * X             10
 * L             50
 * C             100
 * D             500
 * M             1000
 * <p>
 * 例如， 罗马数字 2 写做 II ，即为两个并列的 1。12 写做 XII ，即为 X + II 。 27 写做  XXVII, 即为 XX + V + II 。
 * <p>
 * 通常情况下，罗马数字中小的数字在大的数字的右边。但也存在特例，例如 4 不写做 IIII，而是 IV。数字 1 在数字 5 的左边，所表示的数等于大数 5 减小数 1 得到的数值 4 。同样地，数字 9 表示为 IX。这个特殊的规则只适用于以下六种情况：
 * <p>
 * I 可以放在 V (5) 和 X (10) 的左边，来表示 4 和 9。
 * X 可以放在 L (50) 和 C (100) 的左边，来表示 40 和 90。
 * C 可以放在 D (500) 和 M (1000) 的左边，来表示 400 和 900。
 * <p>
 * 给定一个整数，将其转为罗马数字。输入确保在 1 到 3999 的范围内。
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/6 0006 上午 11:44
 **/
public class IntToRoman {


    //范围1-3999
    public String intToRomanV1(int num) {
        StringBuilder stringBuilder = new StringBuilder();
        //判断千位
        int i = num / 1000;
        if (i > 0) {
            //有千位
            //判断是否是4千和九千
            for (int j = 0; j < i; j++) {
                stringBuilder.append("M");
            }
        }
        //判断百位
        int i1 = num % 1000 / 100;
        if (i1 > 0) {
            if (i1 == 4) {
                stringBuilder.append("CD");
            } else if (i1 == 5) {
                stringBuilder.append("D");
            } else if (i1 == 9) {
                stringBuilder.append("CM");
            } else if (i1 > 5) {
                stringBuilder.append("D");
                for (int j = 0; j < i1 - 5; j++) {
                    stringBuilder.append("C");
                }
            } else {
                for (int j = 0; j < i1; j++) {
                    stringBuilder.append("C");
                }
            }
        }
        //判断十位
        int i2 = num % 100 / 10;
        if (i2 > 0) {
            if (i2 == 4) {
                stringBuilder.append("XL");
            } else if (i2 == 5) {
                stringBuilder.append("L");
            } else if (i2 == 9) {
                stringBuilder.append("XC");
            } else if (i2 > 5) {
                stringBuilder.append("L");
                for (int j = 0; j < i2 - 5; j++) {
                    stringBuilder.append("X");
                }
            } else {
                for (int j = 0; j < i2; j++) {
                    stringBuilder.append("X");
                }
            }
        }
        //判断个位
        int i3 = num % 10;
        if (i3 > 0) {
            if (i3 == 4) {
                stringBuilder.append("IV");
            } else if (i3 == 5) {
                stringBuilder.append("V");
            } else if (i3 == 9) {
                stringBuilder.append("IX");
            } else if (i3 > 5) {
                stringBuilder.append("V");
                for (int j = 0; j < i3 - 5; j++) {
                    stringBuilder.append("I");
                }
            } else {
                for (int j = 0; j < i3; j++) {
                    stringBuilder.append("I");
                }
            }
        }
        return stringBuilder.toString();
    }


    //范围1-3999
    //贪心算法,把所有可能出现的都列举出来
    public String intToRomanV2(int num) {
        StringBuilder stringBuilder = new StringBuilder();
        int[] arrray1 = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] arrray2 = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        for (int i = 0; i < arrray1.length; i++) {
            int i1 = arrray1[i];
            while (num / i1 >= 1) {
                int i2 = num / i1;
                stringBuilder.append(arrray2[i]);
                num -= arrray1[i];
            }
        }
        return stringBuilder.toString();
    }


    public int romanToInt(String roman) {
        int num = 0;
        int[] arrray1 = {1000, 900, 500, 400, 100, 90, 50, 40, 10, 9, 5, 4, 1};
        String[] arrray2 = {"M", "CM", "D", "CD", "C", "XC", "L", "XL", "X", "IX", "V", "IV", "I"};
        for (int i = 0; i < arrray2.length; i++) {
            String s = arrray2[i];
            while (roman.length() > 0 && roman.startsWith(s)) {
                num += arrray1[i];
                roman = roman.substring(s.length());
            }
        }
        return num;
    }


}
