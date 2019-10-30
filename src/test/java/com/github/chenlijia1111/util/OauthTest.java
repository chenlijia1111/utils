package com.github.chenlijia1111.util;

import com.github.chenlijia1111.utils.encrypt.HMacSHA256EncryptUtil;
import com.github.chenlijia1111.utils.oauth.jwt.JWTUtil;
import com.github.chenlijia1111.utils.oauth.qq.QQLoginUtil;
import io.jsonwebtoken.Claims;
import org.junit.Test;

import java.util.Map;

/**
 * @author 陈礼佳
 * @since 2019/9/13 21:35
 */
public class OauthTest {


    @Test
    public void test1() {

        long l = System.currentTimeMillis();

        String jwt = JWTUtil.createJWT("1", "陈礼=佳:\"我嘎嘎/", 1, "qwewqw");
        System.out.println(jwt);
        System.out.println(System.currentTimeMillis() - l);

        long l1 = System.currentTimeMillis();
        System.out.println(JWTUtil.checkFormat(jwt));
        System.out.println(JWTUtil.checkSign(jwt, "qwewqw"));
        System.out.println(JWTUtil.checkExpired(jwt, "qwewqw"));

        Claims claims = JWTUtil.parseJWT(jwt, "qwewqw");
        System.out.println(claims);

        System.out.println((System.currentTimeMillis() - l));
        System.out.println((System.currentTimeMillis() - l1));
    }

    @Test
    public void test2() {
        String s1 = "eyJhbGciOiJIUzI1NiJ9";
        String s2 = "eyJqdGkiOiIxIiwiaWF0IjoxNTY4NDI1NDQzLCJzdWIiOiLpmYjnpLw95L2zOlwi5oiR5ZiO5ZiOLyIsImV4cCI6MTU2ODUxMTg0M30";

        String string = HMacSHA256EncryptUtil.SHA256StringToHexString(s1 + "." + s2, "qwewqw");
        //iwUJPL8CeNheAmypUXrLoxDoYllhaeTdKlk9_g_MlYgwqwq
        System.out.println(string);
    }


    @Test
    public void testqq() {
        //https://graph.qq.com/oauth2.0/show?which=Login&display=pc&
        // response_type=code&
        // state=B169C8F0A4A0BC9A328EF07E77401D53605FF4F8011B452A154CB04D86454969557918812EAC705B922D9920A1EF56CB&
        // client_id=100273020&
        // redirect_uri=https%3A%2F%2Fqq.jd.com%2Fnew%2Fqq%2Fcallback.action%3Fview%3Dnull%26uuid%3Da1c431c6164d43f1b4124adf5fa427f9
        String clientId = "1109533360";
        String clientSecret = "9sbUyp66fNYGOzDE";
        QQLoginUtil qqLoginUtil = new QQLoginUtil();
        Map sa = qqLoginUtil.accessToken(clientId, clientSecret, "B169C8F0A4A0BC9A328EF07E77401D53605FF4F8011B452A154CB04D86454969557918812EAC705B922D9920A1EF56CB", "http://192.168.1.167:8086/jiuyou/system/oauth/callback/qq");
        System.out.println(sa);
    }


}
