package com.github.chenlijia1111.utils.common;

import com.github.chenlijia1111.utils.core.StringUtils;

/**
 * 校验数据 抛出异常
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/4 0004 下午 12:00
 **/
public class AssertUtil {


    /**
     * 判断是否为真 如果不为真 抛出异常
     *
     * @param expression 1
     * @param msg        2
     * @return void
     * @since 下午 12:02 2019/9/4 0004
     **/
    public static void isTrue(boolean expression, String msg) {
        if (!expression) {
            throw new IllegalArgumentException(msg);
        }
    }


    /**
     * 判断字符串是否为空
     * 为空抛出异常
     *
     * @param text 1
     * @param msg  2
     * @return void
     * @since 上午 9:11 2019/9/28 0028
     **/
    public static void hasText(String text, String msg) {
        if (StringUtils.isEmpty(text)) {
            throw new IllegalArgumentException(msg);
        }
    }

}
