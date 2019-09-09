package com.github.chenlijia1111.utils.encrypt;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.enums.CharSetType;
import com.github.chenlijia1111.utils.encrypt.enums.EncryptType;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.util.Base64;

/**
 * AES 加密工具类
 * 微信的AES加密需要另外处理
 * jdk自带的AES算法只支持 AES/ECB/PKCS5Padding 算法
 * 微信小程序的加密算法 AES/CBC/PKCS7Padding 需要引入第三方库才可以实现,已实现
 * @see #encryptWithCBC(String, String, byte[])
 * @see #decryptWithCBC(String, String, byte[])
 * <p>
 * ECB 算法不支持 IV 向量
 * CBC 算法支持 IV 向量
 * 且 IV 向量字节数组必须要 16 个字节 否则会报错
 *
 * AES 还有一些其他的加密算法，暂时没有补充进来
 *
 * @author 陈礼佳
 * @since 2019/9/8 21:26
 */
public class AESUtil {

    //AES/ECB/PKCS5Padding 加密算法
    public static final String AES_ECB_PKCS7PADDING = "AES/ECB/PKCS5Padding";
    //AES/CBC/PKCS7Padding 加密算法
    public static final String AES_CBC_PKCS7PADDING = "AES/CBC/PKCS7Padding";

    static {
        //增加 第三方的 AES算法支持
        Security.addProvider(new BouncyCastleProvider());
    }


    /**
     * ECB 算法加密
     * 返回加密之后的字节数组通过BASE64加密过后的字符串
     * 不支持 IV 偏移向量
     *
     * @param inputStr 加密的字符串
     * @param password 加密的密码
     * @return
     */
    public static String encryptWithECB(String inputStr, String password) {

        //校验参数
        AssertUtil.isTrue(StringUtils.isNotEmpty(inputStr), "加密内容不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(password), "加密密码不能为空");

        try {
            Cipher cipher = Cipher.getInstance(AES_ECB_PKCS7PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(password));
            byte[] bytes = cipher.doFinal(inputStr.getBytes(CharSetType.UTF8.getType()));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ECB 算法解密
     * 不支持 IV 偏移向量
     *
     * @param inputStr 解密的字符串
     * @param password 解密的密码
     * @return
     */
    public static String decryptWithECB(String inputStr, String password) {

        //校验参数
        AssertUtil.isTrue(StringUtils.isNotEmpty(inputStr), "解密内容不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(password), "解密密码不能为空");

        try {
            Cipher cipher = Cipher.getInstance(AES_ECB_PKCS7PADDING);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(password));
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(inputStr));
            return new String(bytes, CharSetType.UTF8.getType());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * CBC 算法加密
     * 返回加密之后的字节数组通过BASE64加密过后的字符串
     *
     * @param inputStr 加密的字符串
     * @param password 加密的密码
     * @param IVBytes  偏移向量 必须16个字节
     * @return
     */
    public static String encryptWithCBC(String inputStr, String password, byte[] IVBytes) {

        //校验参数
        AssertUtil.isTrue(StringUtils.isNotEmpty(inputStr), "加密内容不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(password), "加密密码不能为空");
        AssertUtil.isTrue(null != IVBytes && IVBytes.length == 16, "IV偏移向量必须为16字节");

        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS7PADDING);
            cipher.init(Cipher.ENCRYPT_MODE, getSecretKeySpec(password), new IvParameterSpec(IVBytes));
            byte[] bytes = cipher.doFinal(inputStr.getBytes(CharSetType.UTF8.getType()));
            return Base64.getEncoder().encodeToString(bytes);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * CBC 算法解密
     *
     * @param inputStr 解密的字符串
     * @param password 解密的密码
     * @param IVBytes  偏移向量 必须16个字节
     * @return
     */
    public static String decryptWithCBC(String inputStr, String password, byte[] IVBytes) {

        //校验参数
        AssertUtil.isTrue(StringUtils.isNotEmpty(inputStr), "解密内容不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(password), "解密密码不能为空");
        AssertUtil.isTrue(null != IVBytes && IVBytes.length == 16, "IV偏移向量必须为16字节");

        try {
            Cipher cipher = Cipher.getInstance(AES_CBC_PKCS7PADDING);
            cipher.init(Cipher.DECRYPT_MODE, getSecretKeySpec(password), new IvParameterSpec(IVBytes));
            byte[] bytes = cipher.doFinal(Base64.getDecoder().decode(inputStr));
            return new String(bytes, CharSetType.UTF8.getType());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 根据加密字符串生成加密的密钥
     * 密钥的长度为 16的整数倍数
     * 不过不满16的倍数 不满的部分补0
     *
     * @param password
     * @return
     */
    private static SecretKeySpec getSecretKeySpec(String password) {

        //校验参数
        AssertUtil.isTrue(StringUtils.isNotEmpty(password), "密码不能为空");

        try {

            //处理密码
            byte[] bytes = password.getBytes(CharSetType.UTF8.getType());
            int groups = bytes.length / 16;
            groups = groups + (bytes.length % 16 == 0 ? 0 : 1);

            //新密码字节数组为
            byte[] passwordBytes = new byte[groups * 16];
            System.arraycopy(bytes, 0, passwordBytes, 0, bytes.length);

            KeyGenerator keyGenerator = KeyGenerator.getInstance(EncryptType.AES.getType());
            //AES 的密钥要求为128长度
            keyGenerator.init(128, new SecureRandom(passwordBytes));
            SecretKey secretKey = keyGenerator.generateKey();
            //转换为AES密钥
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getEncoded(), EncryptType.AES.getType());
            return secretKeySpec;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


}
