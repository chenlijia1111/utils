package com.github.chenlijia1111.utils.encrypt;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.NumberUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.enums.CharSetType;
import com.github.chenlijia1111.utils.encrypt.enums.EncryptType;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * HMac_SHA256 加密工具类
 *
 * @author 陈礼佳
 * @since 2019/9/8 11:00
 */
public class HMacSHA256EncryptUtil {


    /**
     * HMac_SHA256 加密字节数组  生成字节数组
     *
     * @param bytes 要加密的字节数组
     * @param key   加密的密钥
     * @throws IllegalArgumentException
     * @return
     */
    public static byte[] SHA256BytesToBytes(byte[] bytes, String key) {

        //校验参数
        AssertUtil.isTrue(StringUtils.isNotEmpty(key), "加密的密钥不能为空");

        try {
            Mac mac = Mac.getInstance(EncryptType.HMAC_SHA256.getType());
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CharSetType.UTF8.getType()), EncryptType.HMAC_SHA256.getType());
            mac.init(secretKeySpec);
            byte[] finalBytes = mac.doFinal(bytes);
            return finalBytes;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * HMac_SHA256 加密字节数组  生成16进制字符串
     *
     * @param bytes
     * @param key   加密的密钥
     * @throws IllegalArgumentException
     * @return
     */
    public static String SHA256BytesToHexString(byte[] bytes, String key) {

        //校验参数
        AssertUtil.isTrue(StringUtils.isNotEmpty(key), "加密的密钥不能为空");

        byte[] md5BytesToBytes = SHA256BytesToBytes(bytes, key);
        if (null != md5BytesToBytes) {
            String s = NumberUtil.byteToHex(md5BytesToBytes);
            return s;
        }
        return null;
    }


    /**
     * HMac_SHA256 加密字符串  生成16进制字符串
     *
     * @param inputStr
     * @param key      加密的密钥
     * @throws IllegalArgumentException
     * @return
     */
    public static String SHA256StringToHexString(String inputStr, String key) {

        //校验参数
        AssertUtil.isTrue(StringUtils.isNotEmpty(key), "加密的密钥不能为空");

        if (StringUtils.isNotEmpty(inputStr)) {
            try {
                return SHA256BytesToHexString(inputStr.getBytes(CharSetType.UTF8.getType()), key);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
