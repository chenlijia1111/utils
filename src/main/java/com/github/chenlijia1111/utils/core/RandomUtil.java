package com.github.chenlijia1111.utils.core;

import java.util.Random;
import java.util.UUID;

/**
 * @Author chenlijia
 * @Date 2019/3/11 0011 上午 10:06
 * @Description 随机工具类
 * @Version 1.0
 **/
public class RandomUtil {


    /**
     * @author chenlijia
     * @Description 创建uuid 字符串
     * @Date 上午 10:17 2019/3/11 0011
     * @return java.lang.String
     **/
    public static String createUUID(){
        String s = UUID.randomUUID().toString().replaceAll("-", "");
        return s;
    }


    /**
     * 创建定长随机四位编号
     * @author chenlijia
     * @since  上午 9:48 2019/3/27 0027
     * @param length 数字编号长度
     * @return java.lang.String
     **/
    public static String createRandomCode(Integer length){
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int i1 = random.nextInt(10);
            stringBuilder.append(i1);
        }
        return stringBuilder.toString();
    }

}
