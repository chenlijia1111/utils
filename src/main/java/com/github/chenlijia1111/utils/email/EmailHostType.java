package com.github.chenlijia1111.utils.email;

/**
 * 邮件发送服务器枚举
 * QQ邮箱 网易邮箱
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/12 0012 上午 9:20
 **/
public enum EmailHostType {

    QQ("smtp.qq.com", "465"), //qq邮箱
    QQ_COMPANY("smtp.exmail.qq.com", "465"), //腾讯企业邮箱
    WANGYI("smtp.163.com", "465"), //网易邮箱
    SINA("smtp.sina.com.cn", "465"), //新浪邮箱
    SINA_VIP("smtp.vip.sina.com", "465"), //新浪VIP邮箱
    SOHU("smtp.sohu.com", "465"), //搜狐邮箱
    GMAIL("smtp.gmail.com", "587") //谷歌邮箱
    ;

    /**
     * 服务器地址
     **/
    private String host;

    /**
     * SSL 端口
     **/
    private String port;


    EmailHostType(String host, String port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }
}
