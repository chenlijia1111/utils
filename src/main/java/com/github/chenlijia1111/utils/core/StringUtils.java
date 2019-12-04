package com.github.chenlijia1111.utils.core;

import com.github.chenlijia1111.utils.common.constant.RegConstant;
import com.github.chenlijia1111.utils.list.Lists;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 *
 * @author chenlijia
 * @since 下午 1:11 2019/9/3 0003
 **/
public class StringUtils {


    /**
     * 判断是否为空
     *
     * @param str 1
     * @author chenlijia
     * @since 下午 1:12 2019/9/3 0003
     **/
    public static boolean isEmpty(String str) {
        return null == str || str.length() == 0;
    }


    /**
     * 判断非空
     *
     * @param str 1
     * @author chenlijia
     * @since 下午 1:13 2019/9/3 0003
     **/
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }


    /**
     * 去除空格 判断是否为空
     *
     * @param str 1
     * @author chenlijia
     * @since 下午 1:12 2019/9/3 0003
     **/
    public static boolean isEmptyTrimWhitespace(String str) {
        if (isNotEmpty(str)) {
            int len = str.length();
            for (int i = 0; i < len; i++) {
                char c = str.charAt(i);
                if (!Character.isWhitespace(c)) {
                    //但凡有一个不是空格的字符串就返回true
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 去除空格 判断非空
     *
     * @param str 1
     * @author chenlijia
     * @since 下午 1:13 2019/9/3 0003
     **/
    public static boolean isNotEmptyTrimWhitespace(String str) {
        return !isEmptyTrimWhitespace(str);
    }


    /**
     * 判断字符串是否是整数
     *
     * @param str 1
     * @author chenlijia
     * @since 下午 7:33 2019/9/3 0003
     **/
    public static boolean isInt(String str) {
        return null != str && Pattern.matches(RegConstant.INT, str);
    }

    /**
     * 判断字符串是否是小数(包含整数)
     *
     * @param str 1
     * @author chenlijia
     * @since 下午 7:33 2019/9/3 0003
     **/
    public static boolean isDouble(String str) {
        return null != str && Pattern.matches(RegConstant.DOUBLE, str);
    }

    /**
     * 判断字符串是否包含了这些字符串
     * 只要包含一个就返回true
     * <p>
     * 可用于判断敏感词汇
     *
     * @param str     1
     * @param strList 2
     * @return boolean
     * @author chenlijia
     * @since 下午 7:54 2019/9/3 0003
     **/
    public static boolean containsStrList(String str, List<String> strList) {
        if (null != str && str.length() > 0 && Lists.isNotEmpty(strList)) {
            for (String s : strList) {
                if (str.contains(s)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 切割字符串为集合
     *
     * @param str         要被切割的字符串
     * @param spiltSymbol 用于切割的标志符号
     * @return java.util.List<java.lang.String>
     * @author chenlijia
     * @since 下午 8:00 2019/9/3 0003
     **/
    public static List<String> spiltStrToList(String str, String spiltSymbol) {
        if (null != str && null != spiltSymbol) {
            String[] split = str.split(spiltSymbol);
            List<String> list = Lists.asList(split);
            return list;
        }
        return null;
    }


    /**
     * 数字转定长字符串
     * <p>
     * 效果: completeIntToFixedLengthStr(DDLS,5,1)  =>>  DDLS00001
     *
     * @param prefix       填充的前缀
     * @param numberLength 希望生成的字符串的长度
     * @param number       数字
     * @param fillContent  循环填充的内容
     * @return java.lang.String
     * @since 下午 7:33 2019/9/4 0004
     **/
    public static String completeIntToFixedLengthStr(String prefix, int numberLength, long number, char fillContent) {
        StringBuilder stringBuilder = new StringBuilder();
        //判断前缀 防止为null
        prefix = prefix == null ? "" : prefix;
        stringBuilder.append(prefix);

        //不够的补0
        String numberString = String.valueOf(number);
        for (int i = 0; i < numberLength - numberString.length(); i++) {
            stringBuilder.append(fillContent);
        }

        stringBuilder.append(numberString);
        return stringBuilder.toString();
    }

    /**
     * 填充字符串
     * <p>
     * 效果: completeIntToFixedLengthStr(DDLS,5,我爱你)  =>>  DDLS00我爱你
     *
     * @param prefix       填充的前缀
     * @param numberLength 希望生成的字符串的长度
     * @param str          内容
     * @param fillContent  要循环填充的内容
     * @return java.lang.String
     * @since 下午 7:33 2019/9/4 0004
     **/
    public static String completeStrToFixedLengthStr(String prefix, int numberLength, String str, char fillContent) {
        StringBuilder stringBuilder = new StringBuilder();
        //判断前缀 防止为null
        prefix = prefix == null ? "" : prefix;
        stringBuilder.append(prefix);

        //不够的补0
        for (int i = 0; i < numberLength - str.length(); i++) {
            stringBuilder.append(fillContent);
        }

        stringBuilder.append(str);
        return stringBuilder.toString();
    }


    /**
     * 异常栈信息转字符串
     *
     * @param exception 1
     * @return java.lang.String
     * @since 下午 2:12 2019/10/16 0016
     **/
    public static String exceptionStackInfoToString(Exception exception) {
        if (null != exception) {
            try (StringWriter sw = new StringWriter();
                 PrintWriter pw = new PrintWriter(sw)) {
                exception.printStackTrace(pw);
                return sw.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 分割字符串-转集合
     *
     * @param str   待分割的字符串
     * @param regex 分割正则
     * @return java.util.List<java.lang.String>
     * @since 上午 9:20 2019/12/4 0004
     **/
    public static List<String> splitToList(String str, String regex) {
        return Lists.asList(splitToArray(str, regex));
    }

    /**
     * 分割字符串-转数组
     *
     * @param str   待分割的字符串
     * @param regex 分割正则
     * @return java.util.List<java.lang.String>
     * @since 上午 9:20 2019/12/4 0004
     **/
    public static String[] splitToArray(String str, String regex) {
        if (StringUtils.isNotEmpty(str)) {
            String[] split = str.split(regex);
            return split;
        }
        return new String[]{};
    }

}
