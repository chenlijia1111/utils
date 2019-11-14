package com.github.chenlijia1111.utils.oauth.wx;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.http.HttpClientUtils;

import java.util.Map;

/**
 * 微信小程序登陆工具类
 *
 * @author 陈礼佳
 * @since 2019/9/14 10:59
 */
public class WXAppletsLoginUtil {

    /**
     * 获取 accessToken
     * 如果accessToken 还没过期就不用去重新调用获取的接口了
     * 只需要调用刷新的接口即可
     *
     * @param appId
     * @param secret
     * @param code
     * @return {
     * "openid":"OPENID", 授权用户唯一标识
     * "unionid":"session_key" 会话密钥
     * "unionid":"o6_bmasdasdsad6_2sgVt7hMZOPfL" 当且仅当该移动应用已获得该用户的userinfo授权时，才会出现该字段
     * "errcode":"errcode", 错误码 -1 系统繁忙，此时请开发者稍候再试 0 请求成功 40029 	code 无效 45011 频率限制，每个用户每分钟100次
     * "errmsg":"errmsg", 错误信息
     * }
     */
    public Map accessToken(String appId, String secret, String code) {

        AssertUtil.isTrue(StringUtils.isNotEmpty(appId), "appId不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(secret), "appSecret不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(code), "code不能为空");

        Map map = HttpClientUtils.getInstance().putParams("appid", appId).
                putParams("secret", secret).
                putParams("js_code", code).
                putParams("grant_type", "authorization_code").
                doGet("https://api.weixin.qq.com/sns/jscode2session").toMap();
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
     *
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
