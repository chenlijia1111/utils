package com.github.chenlijia1111.utils.oauth.wx;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.http.HttpClientUtils;
import org.apache.http.HttpResponse;
import org.apache.http.protocol.HTTP;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * 微信小程序登陆工具类
 *
 * @author 陈礼佳
 * @since 2019/9/14 10:59
 */
public class WXAppletsLoginUtil {

    /**
     * 获取到的accessToken
     */
    private String accessToken;

    /**
     * 获取到accessToken的时间
     */
    private Date accessTime;


    /**
     * 单例对象
     */
    private static volatile WXAppletsLoginUtil wxAppletsLoginUtil;

    private WXAppletsLoginUtil() {
    }

    /**
     * 获取单例对象
     *
     * @return
     */
    public static WXAppletsLoginUtil getInstance() {
        if (Objects.isNull(wxAppletsLoginUtil)) {
            synchronized (WXAppletsLoginUtil.class) {
                if (Objects.isNull(wxAppletsLoginUtil)) {
                    wxAppletsLoginUtil = new WXAppletsLoginUtil();
                }
            }
        }
        return wxAppletsLoginUtil;
    }

    /**
     * 获取 accessToken
     *
     * @param appId
     * @param secret
     * @return {
     * "access_token":"access_token", 授权用户唯一标识
     * "expires_in":"expires_in" 会话密钥
     * "errcode":"errcode", 错误码 -1 系统繁忙，此时请开发者稍候再试 0 请求成功 40029 	code 无效 45011 频率限制，每个用户每分钟100次
     * "errmsg":"errmsg", 错误信息
     * }
     */
    public String accessToken(String appId, String secret) {
        AssertUtil.isTrue(StringUtils.isNotEmpty(appId), "appId不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(secret), "appSecret不能为空");

        if (Objects.nonNull(this.accessToken) && Objects.nonNull(this.accessTime) && System.currentTimeMillis() - this.accessTime.getTime() <= 6000 * 1000L) {
            //有效期是7200秒 6000秒就刷新
            return accessToken;
        }

        //https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
        Map map = HttpClientUtils.getInstance().
                putParams("appid", appId).
                putParams("secret", secret).
                putParams("grant_type", "client_credential").
                doGet("https://api.weixin.qq.com/cgi-bin/token").toMap();

        Object errcode = map.get("errcode");
        if (Objects.nonNull(map) && (Objects.isNull(errcode) || Objects.equals(errcode, 0))) {
            //成功
            this.accessToken = (String) map.get("access_token");
            this.accessTime = new Date();
        }
        return this.accessToken;
    }

    /**
     * 获取小程序码
     * 注意 如果页面不存在的话,会生成小程序失败
     *
     * @param appId
     * @param secret
     * @param pagePath 页面地址 不能携带参数  前面不需要加斜杠  例如 pages/index/index
     * @param secene   页面参数
     * @return 以流的形式返回
     */
    public InputStream appletsQrCode(String appId, String secret, String pagePath, String secene) {

        String accessToken = accessToken(appId, secret);

        //https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=ACCESS_TOKEN
        HttpResponse response = HttpClientUtils.getInstance().
                putParams("scene", secene).
                putParams("page", pagePath).
                putHeader(HTTP.CONTENT_TYPE, "application/json;charset=utf-8").
                doPost("https://api.weixin.qq.com/wxa/getwxacodeunlimit?access_token=" + accessToken).toResponse();
        try {
            InputStream inputStream = response.getEntity().getContent();
            return inputStream;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 获取用户信息
     * <p>
     * {
     * "openid":" OPENID",
     * " nickname": NICKNAME,
     * "sex":"1",
     * "province":"PROVINCE"
     * "city":"CITY",
     * "country":"COUNTRY",
     * "headimgurl":       "http://thirdwx.qlogo.cn/mmopen/g3MonUZtNHkdmzicIlibx6iaFqAc56vxLSUfpb6n5WKSYVY0ChQKkiaJSgQ1dZuTOgvLLrhJbERQQ4eMsv84eavHiaiceqxibJxCfHe/46",
     * "privilege":[ "PRIVILEGE1" "PRIVILEGE2"     ],
     * "unionid": "o6_bmasdasdsad6_2sgVt7hMZOPfL"
     * }
     *
     * @param accessToken 1
     * @param openId      2
     * @return java.util.Map
     * @since 下午 4:46 2019/11/12 0012
     **/
    public Map userInfo(String accessToken, String openId) {
        Map map = HttpClientUtils.getInstance().putParams("openid", openId).
                putParams("access_token", accessToken).
                putParams("lang", "zh_CN").
                doGet("https://api.weixin.qq.com/sns/userinfo").toMap();
        return map;
    }

}
