package com.github.chenlijia1111.util.encrypt;

import com.github.chenlijia1111.utils.encrypt.*;
import org.bouncycastle.jcajce.provider.asymmetric.RSA;
import org.junit.Test;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/9 0009 下午 5:56
 **/
public class Test1 {

    @Test
    public void testAES() {
        byte[] IVBytes = new byte[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0, 8, 7, 6, 5, 4, 3};

        String inputStr = "我是一个中国人";
        String password = "abc123";
        String encrypt = AESUtil.encryptWithCBC(inputStr, password, IVBytes);
        System.out.println(encrypt);
        System.out.println(AESUtil.decryptWithCBC(encrypt, password, IVBytes));
    }


    @Test
    public void testHMacSHA256() {
        System.out.printf(HMacSHA256EncryptUtil.SHA256BytesToHexString("appid=2312312&mch_id=31232131&key=31232131".getBytes(), "31232131"));
    }


    @Test
    public void testMD5() {
        System.out.printf(MD5EncryptUtil.MD5BytesToHexString("appid=2312312&mch_id=31232131&key=31232131".getBytes()));
    }


    @Test
    public void testRSA() {
        RSAUtils rsaUtils = new RSAUtils();
        RSAUtils.RSAKey key = rsaUtils.createKey(1024);
        System.out.println("私钥：" + key.getPrivateKey());
        System.out.println("公钥：" + key.getPublicKey());


        String inputStr = "我是一个中国人";
        String s = RSAUtils.publicEncrypt(inputStr, key.getPublicKey());
        System.out.println("公钥加密后的内容:" + s);
        String s1 = RSAUtils.privateDecrypt(s, key.getPrivateKey());
        System.out.println("私钥解密内容:" + s1);

        String s2 = RSAUtils.privateEncrypt(inputStr, key.getPrivateKey());
        System.out.println("私钥加密内容：" + s2);
        String s3 = RSAUtils.publicDecrypt(s2, key.getPublicKey());
        System.out.println("公钥解密内容:" + s3);

        //签名
        String sign = RSAUtils.sign(key.getPrivateKey(), inputStr);
        boolean b = RSAUtils.checkSign(key.getPublicKey(), inputStr, sign);
        System.out.println(b);
    }


    @Test
    public void testSHA256() {
        System.out.println(SHA256EncryptUtil.SHA256BytesToHexString("appid=2312312&mch_id=31232131&key=31232131".getBytes()));

        RSAUtils rsaUtils = new RSAUtils();
        RSAUtils.RSAKey key = rsaUtils.createKey(512);
        System.out.println(key.getPrivateKey());
        System.out.println(key.getPublicKey());
    }

}
