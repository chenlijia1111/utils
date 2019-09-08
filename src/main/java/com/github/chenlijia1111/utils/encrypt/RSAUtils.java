package com.github.chenlijia1111.utils.encrypt;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.enums.CharSetType;
import com.github.chenlijia1111.utils.encrypt.enums.EncryptType;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * RSA 加密工具类
 * 在本工具类中的公钥私钥字符串都是经过BASE64加密之后的字符串
 * <p>
 * 主要方法如下
 *
 * @author 陈礼佳
 * @see #createKey(int) 创建一对公钥私钥
 * @see #publicEncrypt(String, String) 公钥加密
 * @see #privateEncrypt(String, String) 私钥加密
 * @see #publicDecrypt(String, String) 公钥解密
 * @see #privateDecrypt(String, String) 私钥解密
 * @since 2019/9/8 20:10
 */
public class RSAUtils {


    /**
     * 创建一对 私钥公钥
     * 存取 公钥 私钥 以 base64字符串的形式保存
     *
     * @param keySize keySize 的大小最小为 512
     * @return
     */
    public RSAKey createKey(int keySize) {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(EncryptType.RSA.getType());
            keyPairGenerator.initialize(keySize);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();

            //获取公钥
            PublicKey publicKey = keyPair.getPublic();
            String publicKeyStr = Base64.getEncoder().encodeToString(publicKey.getEncoded());
            //获取私钥
            PrivateKey privateKey = keyPair.getPrivate();
            String privateKeyStr = Base64.getEncoder().encodeToString(privateKey.getEncoded());

            RSAKey rsaKey = new RSAKey();
            rsaKey.setPublicKey(publicKeyStr);
            rsaKey.setPrivateKey(privateKeyStr);
            return rsaKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * RSA 公钥 私钥 保存对象
     */
    public class RSAKey {

        /**
         * 私钥
         */
        private String privateKey;

        /**
         * 公钥
         */
        private String publicKey;

        public String getPrivateKey() {
            return privateKey;
        }

        public void setPrivateKey(String privateKey) {
            this.privateKey = privateKey;
        }

        public String getPublicKey() {
            return publicKey;
        }

        public void setPublicKey(String publicKey) {
            this.publicKey = publicKey;
        }
    }

    /**
     * 通过公钥字符串 获取RSA公钥对象
     *
     * @param publicKeyStr
     * @return
     */
    private static RSAPublicKey getPublicKey(String publicKeyStr) {

        //校验参数
        AssertUtil.isTrue(StringUtils.isNotEmpty(publicKeyStr), "公钥字符串不能为空");

        // 通过X509编码的Key指令获得公钥对象
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(EncryptType.RSA.getType());
            X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr));
            RSAPublicKey publicKey = (RSAPublicKey) keyFactory.generatePublic(x509EncodedKeySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过私钥字符串 获取RSA私钥对象
     *
     * @param privateKeyStr
     * @return
     */
    private static RSAPrivateKey getPrivateKey(String privateKeyStr) {

        //校验参数
        AssertUtil.isTrue(StringUtils.isNotEmpty(privateKeyStr), "私钥字符串不能为空");

        // 通过PKCS#8编码的Key指令获得私钥对象
        try {
            KeyFactory keyFactory = KeyFactory.getInstance(EncryptType.RSA.getType());
            PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr));
            RSAPrivateKey privateKey = (RSAPrivateKey) keyFactory.generatePrivate(pkcs8EncodedKeySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 公钥加密
     * 返回 BASE64 加密后的内容
     *
     * @param inputStr
     * @param publicKey
     * @return
     * @throws IllegalArgumentException
     */
    public static String publicEncrypt(String inputStr, String publicKey) {

        //校验参数
        AssertUtil.isTrue(StringUtils.isNotEmpty(inputStr), "加密内容");
        AssertUtil.isTrue(StringUtils.isNotEmpty(publicKey), "公钥不能为空");

        //开始加密
        try {
            Cipher cipher = Cipher.getInstance(EncryptType.RSA.getType());
            //转化公钥对象
            RSAPublicKey rsaPublicKey = getPublicKey(publicKey);
            //公钥加密模式
            cipher.init(Cipher.ENCRYPT_MODE, rsaPublicKey);
            //进行加密
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
     * 私钥加密
     * 返回 BASE64 加密后的内容
     *
     * @param inputStr
     * @param privateKey
     * @return
     */
    public static String privateEncrypt(String inputStr, String privateKey) {

        //校验参数
        AssertUtil.isTrue(StringUtils.isNotEmpty(inputStr), "加密内容");
        AssertUtil.isTrue(StringUtils.isNotEmpty(privateKey), "私钥不能为空");

        //开始加密
        try {
            Cipher cipher = Cipher.getInstance(EncryptType.RSA.getType());
            //转化私钥对象
            RSAPrivateKey rsaPrivateKey = getPrivateKey(privateKey);
            //私钥加密模式
            cipher.init(Cipher.ENCRYPT_MODE, rsaPrivateKey);
            //进行加密
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
     * 公钥解密
     *
     * @param inputStr  RSA 加密过后的字节数组通过BASE64转化的字符串
     * @param publicKey
     * @return
     */
    public static String publicDecrypt(String inputStr, String publicKey) {

        //校验参数
        AssertUtil.isTrue(StringUtils.isNotEmpty(inputStr), "解密内容");
        AssertUtil.isTrue(StringUtils.isNotEmpty(publicKey), "公钥不能为空");

        //开始解密
        try {
            Cipher cipher = Cipher.getInstance(EncryptType.RSA.getType());
            //转化公钥对象
            RSAPublicKey rsaPublicKey = getPublicKey(publicKey);
            //公钥解密模式
            cipher.init(Cipher.DECRYPT_MODE, rsaPublicKey);
            //进行解密
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
     * 私钥解密
     *
     * @param inputStr   RSA 加密过后的字节数组通过BASE64转化的字符串
     * @param privateKey
     * @return
     */
    public static String privateDecrypt(String inputStr, String privateKey) {

        //校验参数
        AssertUtil.isTrue(StringUtils.isNotEmpty(inputStr), "解密内容");
        AssertUtil.isTrue(StringUtils.isNotEmpty(privateKey), "私钥不能为空");

        //开始解密
        try {
            Cipher cipher = Cipher.getInstance(EncryptType.RSA.getType());
            //转化私钥对象
            RSAPrivateKey rsaPrivateKey = getPrivateKey(privateKey);
            //私钥解密模式
            cipher.init(Cipher.DECRYPT_MODE, rsaPrivateKey);
            //进行解密
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


    public static void main(String[] args) {
        RSAUtils rsaUtils = new RSAUtils();
        RSAKey key = rsaUtils.createKey(1024);
        System.out.println("私钥：" + key.getPrivateKey());
        System.out.println("公钥：" + key.getPublicKey());


        String inputStr = "我是一个中国人";
        String s = publicEncrypt(inputStr, key.getPublicKey());
        System.out.println("公钥加密后的内容:" + s);
        String s1 = privateDecrypt(s, key.getPrivateKey());
        System.out.println("私钥解密内容:" + s1);

        String s2 = privateEncrypt(inputStr, key.getPrivateKey());
        System.out.println("私钥加密内容：" + s2);
        String s3 = publicDecrypt(s2, key.getPublicKey());
        System.out.println("公钥解密内容:" + s3);


    }

}
