package com.github.chenlijia1111.utils.core.cache;

/**
 * 超时类型枚举
 * @author Chen LiJia
 * @since 2020/7/2
 */
public enum TimeOutTypeEnum {

    /**
     * 不会发生超时，永远缓存
     */
    NOT_TIME_OUT,
    /**
     * 固定时间超时，如固定2个小时之后数据就会失效
     */
    FIXED_TIME_OUT,
    /**
     * 延时时间超时，根据上一次请求时间来延续超时时间
     * 如超时时间是 2 个小时，如果在 2 个小时内请求数据，
     * 超时时间继续延续 2 个小时
     */
    DELAY_TIME_OUT,

}
