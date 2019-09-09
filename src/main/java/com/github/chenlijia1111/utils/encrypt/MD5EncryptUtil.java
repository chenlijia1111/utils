package com.github.chenlijia1111.utils.encrypt;

import com.github.chenlijia1111.utils.core.NumberUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.enums.CharSetType;
import com.github.chenlijia1111.utils.encrypt.enums.EncryptType;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * MD5 加密工具类
 *
 * @author 陈礼佳
 * @since 2019/9/8 11:00
 */
public class MD5EncryptUtil {


    /**
     * md5 加密字节数组  生成字节数组
     *
     * @param bytes
     * @return
     */
    public static byte[] MD5BytesToBytes(byte[] bytes) {
        try {
            MessageDigest digest = MessageDigest.getInstance(EncryptType.MD5.getType());
            digest.update(bytes);
            byte[] md = digest.digest();
            return md;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * md5 加密字节数组  生成16进制字符串
     *
     * @param bytes
     * @return
     */
    public static String MD5BytesToHexString(byte[] bytes) {
        byte[] md5BytesToBytes = MD5BytesToBytes(bytes);
        if (null != md5BytesToBytes) {
            String s = NumberUtil.byteToHex(md5BytesToBytes);
            return s;
        }
        return null;
    }


    /**
     * md5 加密字符串  生成16进制字符串
     *
     * @param inputStr
     * @return
     */
    public static String MD5StringToHexString(String inputStr) {
        if (StringUtils.isNotEmpty(inputStr)) {
            try {
                return MD5BytesToHexString(inputStr.getBytes(CharSetType.UTF8.getType()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
