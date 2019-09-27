package com.github.chenlijia1111.utils.common.constant;

/**
 * 常用正则表达式
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/5 0005 下午 1:36
 **/
public class RegConstant {

    /**
     * 手机号
     * 第一位 1
     * 第二位 3 4 5 7 8
     * 第三位 1 2 3 4 5 6 7 8 9 0
     *
     * @since 下午 1:42 2019/9/5 0005
     **/
    public static final String MOBILE_PHONE = "^1[3|4|5|7|8][0-9]\\d{4,8}$";

    /**
     * 邮箱
     * username@domain
     *
     * @since 下午 1:42 2019/9/5 0005
     **/
    public static final String E_MAIL = "\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*";

    /**
     * 身份证
     * 362321199706101018
     *
     * @since 下午 1:26 2019/9/27 0027
     **/
    public static final String ID_CARD = "(^[1-9]\\d{5}(18|19|20)\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$)|" +
            "(^[1-9]\\d{5}\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}$)";

}
