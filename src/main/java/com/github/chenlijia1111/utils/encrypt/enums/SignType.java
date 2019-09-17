package com.github.chenlijia1111.utils.encrypt.enums;

/**
 * 签名类型
 *
 * @author 陈礼佳
 * @since 2019/9/15 17:13
 */
public enum SignType {

    MD5_WITH_RSA("MD5withRSA"),
    SHA256_WITH_RSA("SHA256WithRSA"),
    SHA1_WITH_RSA("SHA1WithRSA"),
    ;

    private String type;

    SignType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
