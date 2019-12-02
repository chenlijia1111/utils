package com.github.chenlijia1111.utils.core;

import org.joda.time.DateTime;

import java.util.Random;
import java.util.UUID;

/**
 * @Author chenlijia
 * @Date 2019/3/11 0011 上午 10:06
 * @Description 随机工具类
 * @Version 1.0
 **/
public class RandomUtil {

    //名称随机值
    public static final IDUtil nameRandom = new IDUtil(30, 1);

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
     *
     * @return java.lang.String
     * @since 上午 10:17 2019/3/11 0011
     **/
    public static String createRandomName() {
        StringBuilder sb = new StringBuilder();
        String timeStr = DateTime.now().toString("yyyyMMddHHmmss");
        sb.append(timeStr);
        sb.append(nameRandom.nextId());
        return sb.toString();
    }


    /**
     * 创建定长随机长度编号
     *
     * @param length 数字编号长度
     * @return java.lang.String
     * @author chenlijia
     * @since 上午 9:48 2019/3/27 0027
     **/
    public static String createRandomCode(Integer length) {
        Random random = new Random();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int i1 = random.nextInt(10);
            stringBuilder.append(i1);
        }
        return stringBuilder.toString();
    }

}
