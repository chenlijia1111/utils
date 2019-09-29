package com.github.chenlijia1111.utils.core;

import com.github.chenlijia1111.utils.common.constant.BooleanConstant;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * 数据转换工具类
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/21 0021 上午 10:51
 **/
public class CastUtil {


    /**
     * 将对象转为字符串
     *
     * @param obj          1
     * @param defaultValue 默认值
     * @return java.lang.String
     * @since 上午 10:54 2019/9/21 0021
     **/
    public static String castToStr(Object obj, String defaultValue) {
        return null != obj ? obj.toString() : defaultValue;
    }


    /**
     * 将对象转为字符串
     *
     * @param obj 1
     * @return java.lang.String
     * @since 上午 10:54 2019/9/21 0021
     **/
    public static String castToStr(Object obj) {
        return castToStr(obj, "");
    }


    /**
     * 字符串转换为整数
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return java.lang.Integer
     * @since 上午 11:13 2019/9/21 0021
     **/
    public static Integer strCastToInteger(String str, Integer defaultValue) {
        //只有在不为空且是数字的时候才进行转换,否则返回默认值
        if (StringUtils.isNotEmpty(str) && StringUtils.isInt(str)) {
            return Integer.valueOf(str);
        }
        return defaultValue;
    }

    /**
     * 字符串转换为整数
     *
     * @param str 字符串
     * @return java.lang.Integer
     * @since 上午 11:13 2019/9/21 0021
     **/
    public static Integer strCastToInteger(String str) {
        //转换失败返回null
        return strCastToInteger(str, null);
    }


    /**
     * 字符串转换为小数
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return java.lang.Double
     * @since 上午 11:13 2019/9/21 0021
     **/
    public static Double strCastToDouble(String str, Double defaultValue) {
        //只有在不为空且是数字的时候才进行转换,否则返回默认值
        if (StringUtils.isNotEmpty(str) && StringUtils.isDouble(str)) {
            return Double.parseDouble(str);
        }
        return defaultValue;
    }

    /**
     * 字符串转换为小数
     *
     * @param str 字符串
     * @return java.lang.Double
     * @since 上午 11:13 2019/9/21 0021
     **/
    public static Double strCastToDouble(String str) {
        //转换失败返回null
        return strCastToDouble(str, null);
    }

    /**
     * 字符串转换为长整形
     *
     * @param str          字符串
     * @param defaultValue 默认值
     * @return java.lang.Integer
     * @since 上午 11:13 2019/9/21 0021
     **/
    public static Long strCastToLong(String str, Long defaultValue) {
        //只有在不为空且是数字的时候才进行转换,否则返回默认值
        if (StringUtils.isNotEmpty(str) && StringUtils.isInt(str)) {
            return Long.valueOf(str);
        }
        return defaultValue;
    }

    /**
     * 字符串转换为长整形
     *
     * @param str 字符串
     * @return java.lang.Integer
     * @since 上午 11:13 2019/9/21 0021
     **/
    public static Long strCastToLong(String str) {
        //转换失败返回null
        return strCastToLong(str, null);
    }


    /**
     * 将字符串转换为 boolean 值
     *
     * @param str          1
     * @param defaultValue 默认值
     * @return java.lang.Boolean
     * @since 上午 11:24 2019/9/21 0021
     **/
    public static Boolean strCastToBoolean(String str, Boolean defaultValue) {
        if (StringUtils.isNotEmpty(str)) {
            if (StringUtils.isInt(str)) {
                //0为false
                if (Objects.equals(BooleanConstant.NO_STR, str)) {
                    return false;
                }
                //1为true
                if (Objects.equals(BooleanConstant.YES_STR, str)) {
                    return true;
                }
            } else {
                if (Objects.equals("false", str.toLowerCase())) {
                    return false;
                }
                if (Objects.equals("true", str.toLowerCase())) {
                    return true;
                }
            }
        }
        return defaultValue;
    }


    /**
     * 将字符串转换为 boolean 值
     *
     * @param str 1
     * @return java.lang.Boolean
     * @since 上午 11:24 2019/9/21 0021
     **/
    public static Boolean strCastToBoolean(String str) {
        return strCastToBoolean(str, null);
    }


    /**
     * double 转字符串
     * 四舍五入
     *
     * @param d      小数
     * @param pointLength 保留的小数点长度
     * @return java.lang.String
     * @since 下午 8:17 2019/9/26 0026
     **/
    public static String doubleCastToStr(double d, int pointLength) {
        BigDecimal bigDecimal = new BigDecimal(d);
        bigDecimal = bigDecimal.setScale(pointLength, BigDecimal.ROUND_HALF_UP);
        String s = bigDecimal.toString();
        return s;
    }


}
