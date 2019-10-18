package com.github.chenlijia1111.utils.oauth.qq;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.common.constant.ContentTypeConstant;
import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.http.HttpClientUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * qq 登录
 * 网页登陆
 *
 * @author 陈礼佳
 * @since 2019/9/14 11:00
 */
public class QQLoginUtil {


    private String accessToken;

    private String refreshToken;

    /**
     * 过期时间
     */
    private Date expireTime;


    /**
     * 获取 accessToken
     * 如果accessToken 还没过期就不用去重新调用获取的接口了
     * 只需要调用刷新的接口即可
     *
     * @param appId
     * @param secret
     * @param code
     * @param redirectUrl
     * @return {
     * "access_token":"ACCESS_TOKEN", 接口调用凭证
     * "expires_in":7200, access_token接口调用凭证超时时间，单位（秒）
     * "refresh_token":"REFRESH_TOKEN", 用户刷新access_token
     * }
     */
    public Map accessToken(String appId, String secret, String code, String redirectUrl) {

        AssertUtil.isTrue(StringUtils.isNotEmpty(appId), "appId不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(secret), "appSecret不能为空");
        //如果是第一次,需要校验code不能为空
        AssertUtil.isTrue((Objects.nonNull(this.expireTime)
                && System.currentTimeMillis() <= this.expireTime.getTime() - 10 * 1000L) ||
                StringUtils.isNotEmpty(code), "code不能为空");

        //判断之前是否有请求过了，如果请求过了，就直接刷新就好了
        //在 10 秒之前用刷新接口
        if (StringUtils.isNotEmpty(this.accessToken) && StringUtils.isNotEmpty(this.refreshToken)
                && Objects.nonNull(this.expireTime) && System.currentTimeMillis() <= this.expireTime.getTime() - 10 * 1000L) {
            Map map1 = refreshAccessToken(appId, secret, this.refreshToken, this.accessToken);
            return map1;
        }

        String s = HttpClientUtils.getInstance().putParams("client_id", appId).
                putParams("client_secret", secret).
                putParams("code", code).
                putParams("grant_type", "authorization_code").
                putParams("redirect_uri", redirectUrl).
                doGet("https://graph.qq.com/oauth2.0/token").toString();


        //返回callback( {"client_id":"YOUR_APPID","openid":"YOUR_OPENID"} )   或者  callback( {"error":100019,"error_description":"code to access token error"} )
        //判断是否成功
        //进行截取
        s = s.substring(s.indexOf("{"), s.lastIndexOf("}") + 1);
        System.out.println(s);
        Map map = JSONUtil.strToObj(s, HashMap.class);
        Object errcode = map.get("error");
        if (Objects.isNull(errcode)) {
            //没有发生错误
            //主动刷新过期时间
            Object refresh_token = map.get("refresh_token");
            Object accessToken = map.get("access_token");
            Object expiresIn = map.get("expires_in");
            Long aLong = Long.valueOf(expiresIn.toString());
            this.refreshToken = refresh_token.toString();
            this.accessToken = accessToken.toString();
            this.expireTime = new Date(System.currentTimeMillis() + aLong * 1000);

            Map map1 = refreshAccessToken(appId, secret, this.refreshToken, this.accessToken);
            return map1;
        }

        return map;
    }


    /**
     * 刷新token
     * 顺便请求openId返回
     *
     * @param appId
     * @param secret
     * @param refreshToken 请求accessToken的时候返回的
     * @param accessToken
     * @return
     */
    private Map refreshAccessToken(String appId, String secret, String refreshToken, String accessToken) {
        Map map = HttpClientUtils.getInstance().
                putParams("client_id", appId).
                putParams("client_secret", secret).
                putParams("grant_type", "refresh_token").
                putParams("refresh_token", refreshToken).
                doGet("https://graph.qq.com/oauth2.0/token").toMap();

        if (Objects.isNull(map.get("code"))) {
            //请求成功
            //顺便获取openId返回
            Map map1 = HttpClientUtils.getInstance().
                    putParams("access_token", accessToken).
                    doGet("https://graph.qq.com/oauth2.0/me").toMap();
            Object openid = map1.get("openid");
            map.put("openid", openid.toString());
        }
        return map;
    }

}
