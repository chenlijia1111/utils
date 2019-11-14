package com.github.chenlijia1111.utils.oauth.wx;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.http.HttpClientUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * 微信APP登陆工具类
 * 微信 app js 公众号
 * 获取accessToken 的请求路径都是一样的
 * 小程序获取 accessToken 请转至 {@link}
 *
 * @author 陈礼佳
 * @since 2019/9/14 10:59
 */
public class WXCommonLoginUtil {


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
     * @return {
     * "access_token":"ACCESS_TOKEN", 接口调用凭证
     * "expires_in":7200, access_token接口调用凭证超时时间，单位（秒）
     * "refresh_token":"REFRESH_TOKEN", 用户刷新access_token
     * "openid":"OPENID", 授权用户唯一标识
     * "scope":"SCOPE", 用户授权的作用域，使用逗号（,）分隔
     * "unionid":"o6_bmasdasdsad6_2sgVt7hMZOPfL" 当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
     * }
     */
    public Map accessToken(String appId, String secret, String code) {

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
            Map map1 = refreshAccessToken(appId, this.refreshToken);
            return map1;
        }

        Map map = HttpClientUtils.getInstance().putParams("appid", appId).
                putParams("secret", secret).
                putParams("code", code).
                putParams("grant_type", "authorization_code").
                doGet("https://api.weixin.qq.com/sns/oauth2/access_token").toMap();

        //判断是否成功
        Object errcode = map.get("errcode");
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

            Map map1 = refreshAccessToken(appId, this.refreshToken);
            return map1;
        }

        return map;
    }


    /**
     * 刷新token
     *
     * @param appId
     * @param refreshToken 请求accessToken的时候返回的
     * @return
     */
    private Map refreshAccessToken(String appId, String refreshToken) {
        Map map = HttpClientUtils.getInstance().putParams("appid", appId).
                putParams("grant_type", "refresh_token").
                putParams("refresh_token", refreshToken).
                doGet("https://api.weixin.qq.com/sns/oauth2/refresh_token").toMap();
        return map;
    }


    /**
     * 获取用户信息
     *
     * {
     *   "openid":" OPENID",
     *   " nickname": NICKNAME,
     *   "sex":"1",
     *   "province":"PROVINCE"
     *   "city":"CITY",
     *   "country":"COUNTRY",
     *   "headimgurl":       "http://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
     *   "privilege":[ "PRIVILEGE1" "PRIVILEGE2"     ],
     *   "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     * @since 下午 4:46 2019/11/12 0012
     * @param accessToken 1
     * @param openId 2
     * @return java.util.Map
     **/
    public Map userInfo(String accessToken, String openId) {
        Map map = HttpClientUtils.getInstance().putParams("openid", openId).
                putParams("access_token", accessToken).
                putParams("lang", "zh_CN").
                doGet("https://api.weixin.qq.com/sns/userinfo").toMap();
        return map;
    }


}
