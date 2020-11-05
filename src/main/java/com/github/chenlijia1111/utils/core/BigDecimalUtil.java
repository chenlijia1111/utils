package com.github.chenlijia1111.utils.core;

import java.math.BigDecimal;

/**
 * bigDecimal 工具类
 * double 转 bigDecimal 最好先将 double 转为 字符串
 *
 * @author Chen LiJia
 * @since 2020/9/30
 */
public class BigDecimalUtil {

    //精度
    public static int SCALE = 2;
    //取值方式 四舍五入
    public static int ROUNDING_MODE = BigDecimal.ROUND_HALF_UP;

    /**
     * 加法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal add(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return add(b1, b2);
    }

    /**
     * 加法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal add(BigDecimal v1, BigDecimal v2) {
        return v1.add(v1);
    }

    /**
     * 减法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal sub(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return sub(b1, b2);
    }

    /**
     * 减法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal sub(BigDecimal v1, BigDecimal v2) {
        return v1.subtract(v1);
    }

    /**
     * 乘法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal mul(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return mul(b1, b2);
    }

    /**
     * 乘法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal mul(BigDecimal v1, BigDecimal v2) {
        v1 = v1.setScale(SCALE, ROUNDING_MODE);
        v2 = v2.setScale(SCALE, ROUNDING_MODE);
        BigDecimal multiply = v1.multiply(v2);
        multiply = multiply.setScale(SCALE, ROUNDING_MODE);
        return multiply;
    }

    /**
     * 除法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal divide(double v1, double v2) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return divide(b1, b2);
    }

    /**
     * 除法
     *
     * @param v1
     * @param v2
     * @return
     */
    public static BigDecimal divide(BigDecimal v1, BigDecimal v2) {
        v1 = v1.setScale(SCALE, ROUNDING_MODE);
        v2 = v2.setScale(SCALE, ROUNDING_MODE);
        BigDecimal multiply = v1.divide(v2, 2, ROUNDING_MODE);
        multiply = multiply.setScale(SCALE, ROUNDING_MODE);
        return multiply;
    }

}
