package com.github.chenlijia1111.utils.encrypt.enums;

/**
 * 签名加密类型
 *
 * @author 陈礼佳
 * @since 2019/9/8 11:04
 */
public enum EncryptType {

    MD5("MD5"),
    SHA256("SHA-256"),
    HMAC_SHA256("HmacSHA256"),
    RSA("RSA");

    private String type;


    EncryptType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
