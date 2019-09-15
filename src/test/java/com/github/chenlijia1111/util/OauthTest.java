package com.github.chenlijia1111.util;

import com.github.chenlijia1111.utils.encrypt.HMacSHA256EncryptUtil;
import com.github.chenlijia1111.utils.oauth.jwt.JWTUtil;
import io.jsonwebtoken.Claims;
import org.junit.Test;

/**
 * @author 陈礼佳
 * @since 2019/9/13 21:35
 */
public class OauthTest {


    @Test
    public void test1() {

        long l = System.currentTimeMillis();

        String jwt = JWTUtil.createJWT("1", "陈礼=佳:\"我嘎嘎/", 1000 * 60 * 60 * 24, "qwewqw");
        System.out.println(jwt);
        System.out.println(System.currentTimeMillis() - l);

        long l1 = System.currentTimeMillis();
        System.out.println(JWTUtil.checkFormat(jwt));
        System.out.println(JWTUtil.checkSign(jwt,"qwewqw"));
        System.out.println(JWTUtil.checkExpired(jwt,"qwewqw"));

        Claims claims = JWTUtil.parseJWT(jwt, "qwewqw");
        System.out.println(claims);

        System.out.println((System.currentTimeMillis() - l));
        System.out.println((System.currentTimeMillis() - l1));
    }

    @Test
    public void test2(){
        String s1 = "eyJhbGciOiJIUzI1NiJ9";
        String s2 = "eyJqdGkiOiIxIiwiaWF0IjoxNTY4NDI1NDQzLCJzdWIiOiLpmYjnpLw95L2zOlwi5oiR5ZiO5ZiOLyIsImV4cCI6MTU2ODUxMTg0M30";

        String string = HMacSHA256EncryptUtil.SHA256StringToHexString(s1 + "." + s2, "qwewqw");
        //iwUJPL8CeNheAmypUXrLoxDoYllhaeTdKlk9_g_MlYgwqwq
        System.out.println(string);
    }


}
