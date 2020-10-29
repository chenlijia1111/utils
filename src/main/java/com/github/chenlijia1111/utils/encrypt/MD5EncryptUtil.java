package com.github.chenlijia1111.utils.encrypt;

import com.github.chenlijia1111.utils.core.NumberUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.enums.CharSetType;
import com.github.chenlijia1111.utils.encrypt.enums.EncryptType;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;

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

    /**
     * 将输入流转化为 十六进制 字符串
     * 支持大文件输入流
     *
     * 注意：一般输入流是只支持读取一次的，web开发需要将输入流进行包装
     * {@link com.github.chenlijia1111.utils.http.request.RequestWrapper}
     *
     * 如果是文件输入流的话，就要另外构建一个输入流来计算 md5 值，不然输入流被读取了一次之后就没了
     *
     * @param inputStream
     * @return
     */
    public static String MD5InputStreamToHexString(InputStream inputStream) {
        if (Objects.nonNull(inputStream)) {
            try {
                MessageDigest digest = MessageDigest.getInstance(EncryptType.MD5.getType());
                byte[] bytes = new byte[1024 * 4];
                int read = 0;
                while (read != -1) {
                    read = inputStream.read(bytes);
                    if (read != -1) {
                        digest.update(bytes, 0, read);
                    }
                }
                byte[] md5Bytes = digest.digest();
                return NumberUtil.byteToHex(md5Bytes);
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        return null;
    }

}
