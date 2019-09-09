package com.github.chenlijia1111.utils.core;

/**
 * 数字工具类
 * 包含 10进制 16进制 2进制 以及字节数组之间的相互转换
 * 以及数据的常用方法
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/6 0006 下午 8:48
 **/
public class NumberUtil {

    /**
     * byte 数组转16进制字符串
     * 原理  一个字节由8位组成
     * 16 进制和 2 进制的关系
     * 0xFF 可以表示为 11111111
     * 0x0F 可以表示为 00001111
     * 即16进制 的一位可以表示 2进制里的 4位
     * <p>
     * 所以一个字节可以跟 0xFF 做 & 运算 得出他的 10 进制值 再转换为 16进制
     * <p>
     * 如果想要方便显示阅读 可以自己在生成后的 16 进制前面加上 0X
     *
     * @param bytes 1
     * @return java.lang.String
     * @since 下午 8:50 2019/9/6 0006
     **/
    public static String byteToHex(byte[] bytes) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() < 2) {
                sb.append(0);
            }
            sb.append(hex);
        }
        return sb.toString().toUpperCase();
    }


}
