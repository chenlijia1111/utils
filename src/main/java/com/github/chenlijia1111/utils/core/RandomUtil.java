package com.github.chenlijia1111.utils.core;

import java.util.Random;
import java.util.UUID;

/**
 * 随机工具类
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/3/11 0011 上午 10:06
 **/
public class RandomUtil {

    //名称随机值
    private static final IDUtil NAME_RANDOM_GENERATOR = new IDUtil(30, 1);

    /**
     * 随机字符串的可选数组
     */
    private static final char[] RANDOM_CHAR_ARRAY = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 创建uuid 字符串
     *
     * @return java.lang.String
     * @since 上午 10:17 2019/3/11 0011
     **/
    public static String createUUID() {
        String s = UUID.randomUUID().toString().replaceAll("-", "");
        return s;
    }

    /**
     * 创建名称
     * 时间+随机数
     * 用于文件名
     *
     * @return java.lang.String
     * @since 上午 10:17 2019/3/11 0011
     **/
    public static String createRandomName() {
        return String.valueOf(NAME_RANDOM_GENERATOR.nextId());
    }


    /**
     * 创建定长随机长度编号
     * 默认生成随机数字
     *
     * @param length 数字编号长度
     * @return java.lang.String
     * @author chenlijia
     * @since 上午 9:48 2019/3/27 0027
     **/
    public static String createRandomCode(Integer length) {
        return createRandomCode(length, RANDOM_CHAR_ARRAY);
    }

    /**
     * 创建定长随机长度编号
     *
     * @param length          数字编号长度
     * @param randomCharArray 生成的字符串可选范围
     * @return java.lang.String
     * @author chenlijia
     * @since 上午 9:48 2019/3/27 0027
     **/
    public static String createRandomCode(Integer length, char[] randomCharArray) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(randomCharArray.length);
            stringBuilder.append(randomCharArray[randomIndex]);
        }
        return stringBuilder.toString();
    }

}
