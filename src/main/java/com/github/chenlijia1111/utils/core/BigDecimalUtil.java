package com.github.chenlijia1111.utils.core;

import com.github.chenlijia1111.utils.common.AssertUtil;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * bigDecimal 工具类
 * double 转 bigDecimal 最好先将 double 转为 字符串
 *
 * @author Chen LiJia
 * @since 2020/9/30
 */
public class BigDecimalUtil {

    //最终结果精度
    private Integer resultScale = 2;
    //取值方式 四舍五入
    private Integer roundingMode = BigDecimal.ROUND_HALF_UP;

    // 当前结果
    private BigDecimal currentResult;

    public BigDecimalUtil(BigDecimal currentResult) {
        AssertUtil.notNull(currentResult, "数据为空");
        this.currentResult = currentResult;
    }

    /**
     * 加法
     *
     * @param v
     * @return
     */
    public BigDecimalUtil add(double v) {
        BigDecimal b1 = new BigDecimal(Double.toString(v));
        this.currentResult = this.currentResult.add(b1);
        return this;
    }

    /**
     * 加法
     *
     * @param v
     * @return
     */
    public BigDecimalUtil add(BigDecimal v) {
        this.currentResult = this.currentResult.add(v);
        return this;
    }

    /**
     * 减法
     *
     * @param v
     * @return
     */
    public BigDecimalUtil sub(double v) {
        BigDecimal b = new BigDecimal(Double.toString(v));
        this.currentResult = this.currentResult.subtract(b);
        return this;
    }

    /**
     * 减法
     *
     * @param v
     * @return
     */
    public BigDecimalUtil sub(BigDecimal v) {
        this.currentResult = this.currentResult.subtract(v);
        return this;
    }

    /**
     * 乘法
     *
     * @param v
     * @return
     */
    public BigDecimalUtil mul(double v) {
        BigDecimal b = new BigDecimal(Double.toString(v));
        this.currentResult = this.currentResult.multiply(b);
        return this;
    }

    /**
     * 乘法
     *
     * @param v
     * @return
     */
    public BigDecimalUtil mul(BigDecimal v) {
        this.currentResult = this.currentResult.multiply(v);
        return this;
    }

    /**
     * 除法
     * 除法必须要有舍入模式的，不然碰到除不尽的会报错
     *
     * @param v
     * @return
     */
    public BigDecimalUtil divide(double v) {
        BigDecimal b = new BigDecimal(Double.toString(v));
        this.currentResult = this.currentResult.divide(b, 10, this.roundingMode);
        return this;
    }

    /**
     * 除法
     * 除法必须要有舍入模式的，不然碰到除不尽的会报错
     * @param v
     * @return
     */
    public BigDecimalUtil divide(BigDecimal v) {
        this.currentResult = this.currentResult.divide(v, 10, this.roundingMode);
        return this;
    }


    /**
     * 判断 bigDecimal 是否是整数
     * true 表示是整数
     * false 表示不是整数
     *
     * @param bigDecimal
     * @return
     */
    public static boolean intStatus(BigDecimal bigDecimal) {
        if (Objects.nonNull(bigDecimal)) {
            return new BigDecimal(bigDecimal.intValue()).compareTo(bigDecimal) == 0;
        }
        return false;
    }

    /**
     * 获取最终计算结果
     *
     * @return
     */
    public BigDecimal getCurrentResult() {
        this.currentResult = this.currentResult.setScale(this.resultScale, this.roundingMode);
        return this.currentResult;
    }

    /**
     * 设置最终结果精度
     *
     * @param resultScale
     */
    public BigDecimalUtil setResultScale(Integer resultScale) {
        this.resultScale = resultScale;
        return this;
    }

    /**
     * 设置获取最终结果的方式，默认是四舍五入
     *
     * @param roundingMode
     * @return
     */
    public BigDecimalUtil setRoundingMode(Integer roundingMode) {
        this.roundingMode = roundingMode;
        return this;
    }
}
