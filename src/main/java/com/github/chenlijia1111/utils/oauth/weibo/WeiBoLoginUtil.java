package com.github.chenlijia1111.utils.oauth.weibo;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.http.HttpClientUtils;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * 微博登陆工具类
 * 微博的accessToken是与用户关绑定的，
 * 即accessToken是通过用户的code获取的，那么就可以通过这个code去做一些其他的操作
 * 如：获取这个用户的信息，发微博，关注等等
 *
 * @author Chen LiJia
 * @since 2020/5/30
 */
public class WeiBoLoginUtil {

    /**
     * 微博用户id
     */
    private Integer uid;

    private String accessToken;

    /**
     * 过期时间
     */
    private Date expireTime;

    public WeiBoLoginUtil() {
    }

    public WeiBoLoginUtil(Integer uid, String accessToken, Date expireTime) {
        this.uid = uid;
        this.accessToken = accessToken;
        this.expireTime = expireTime;
    }

    /**
     * 获取accessToken
     * 请求url:https://api.weibo.com/oauth2/access_token POST
     * <p>
     * 返回正确信息示例：
     * {@code
     * {
     * "access_token": "ACCESS_TOKEN",
     * "expires_in": 1234,
     * "remind_in":"798114",
     * "uid":"12341234"
     * }
     * }
     * 返回错误信息示例：
     * {@code
     * {
     * "request":"/oauth2/access_token",
     * "error_code":21322,
     * "error":"redirect_uri_mismatch",
     * "error_uri":"/oauth2/access_token"
     * }
     * }
     * <p>
     * 通过是否有 err_code 来判断是否错误
     *
     * @param client_id     申请的app参数
     * @param client_secret 申请的app参数
     * @param code          回调拿到的code
     * @param redirect_uri  回调地址，需需与注册应用里的回调地址一致。
     * @return
     */
    public Map accessToken(String client_id, String client_secret, String code, String redirect_uri) {
        AssertUtil.hasText(client_id, "client_id为空");
        AssertUtil.hasText(client_secret, "client_secret为空");
        AssertUtil.hasText(code, "code为空");
        AssertUtil.hasText(redirect_uri, "redirect_uri为空");

        Map map = HttpClientUtils.getInstance().
                putParams("client_id", client_id).
                putParams("client_secret", client_secret).
                putParams("grant_type", "authorization_code").
                putParams("code", code).
                putParams("redirect_uri", redirect_uri).
                doPost("https://api.weibo.com/oauth2/access_token").
                toMap();

        if(Objects.nonNull(map) && !map.containsKey("error_code")){
            //没报错，进行赋值
            Object access_token = map.get("access_token");
            Object expires_in = map.get("expires_in");
            Object uid = map.get("uid");
            this.uid = Integer.valueOf(uid.toString());
            this.accessToken = access_token.toString();
            //判断过期时间
            Integer expireSeconds = Integer.valueOf(expires_in.toString());
            this.expireTime = new Date(System.currentTimeMillis() + expireSeconds * 1000);
        }
        return map;
    }

    /**
     * 查询用户access_token的授权相关信息，包括授权时间，过期时间和scope权限。
     * 请求地址:https://api.weibo.com/oauth2/get_token_info POST
     * <p>
     * 返回正确信息：
     * {@code
     * {
     * "uid": 1073880650,
     * "appkey": 1352222456,
     * "scope": null,
     * "create_at": 1352267591,
     * "expire_in": 157679471
     * }
     * }
     * 如果返回正确信息，通过判断 expire_in：access_token的剩余时间，单位是秒数，如果返回的时间是负数，代表授权已经过期。
     * <p>
     * 返回错误信息：
     * {@code
     * {
     * "request": "/oauth2/get_token_info",
     * "error_code": 21317,
     * "error": "Oauth Error: token_rejected:: token =195395370"
     * }
     * }
     * 可以根据err_code 进行判断是否错误
     *
     * @param access_token
     * @return
     */
    public Map getTokenInfo(String access_token) {
        AssertUtil.hasText(access_token, "access_token为空");

        Map map = HttpClientUtils.getInstance().
                putParams("access_token", access_token).
                doPost("https://api.weibo.com/oauth2/get_token_info").
                toMap();
        if(Objects.nonNull(map) && !map.containsKey("error_code")){
            //没报错，进行赋值
            Object expires_in = map.get("expires_in");
            Object uid = map.get("uid");
            this.uid = Integer.valueOf(uid.toString());
            this.accessToken = access_token;
            //判断过期时间
            Integer expireSeconds = Integer.valueOf(expires_in.toString());
            this.expireTime = new Date(System.currentTimeMillis() + expireSeconds * 1000);
        }
        return map;
    }

    /**
     * 获取微博用户信息接口
     * 请求地址：https://api.weibo.com/2/users/show.json GET
     * 返回正确结果：
     * {@code
     *    {
     *     "id": 1404376560,
     *     "screen_name": "zaku",
     *     "name": "zaku",
     *     "province": "11",
     *     "city": "5",
     *     "location": "北京 朝阳区",
     *     "description": "人生五十年，乃如梦如幻；有生斯有死，壮士复何憾。",
     *     "url": "http://blog.sina.com.cn/zaku",
     *     "profile_image_url": "http://tp1.sinaimg.cn/1404376560/50/0/1",
     *     "domain": "zaku",
     *     "gender": "m",
     *     "followers_count": 1204,
     *     "friends_count": 447,
     *     "statuses_count": 2908,
     *     "favourites_count": 0,
     *     "created_at": "Fri Aug 28 00:00:00 +0800 2009",
     *     "following": false,
     *     "allow_all_act_msg": false,
     *     "geo_enabled": true,
     *     "verified": false,
     *     "status": {
     *         "created_at": "Tue May 24 18:04:53 +0800 2011",
     *         "id": 11142488790,
     *         "text": "我的相机到了。",
     *         "source": "<a href="http://weibo.com" rel="nofollow">新浪微博</a>",
     *         "favorited": false,
     *         "truncated": false,
     *         "in_reply_to_status_id": "",
     *         "in_reply_to_user_id": "",
     *         "in_reply_to_screen_name": "",
     *         "geo": null,
     *         "mid": "5610221544300749636",
     *         "annotations": [],
     *         "reposts_count": 5,
     *         "comments_count": 8
     *     },
     *     "allow_all_comment": true,
     *     "avatar_large": "http://tp1.sinaimg.cn/1404376560/180/0/1",
     *     "verified_reason": "",
     *     "follow_me": false,
     *     "online_status": 0,
     *     "bi_followers_count": 215
     *    }
     * }
     *
     * 返回错误代码如下：
     * {@code
     *   {
     *      "request":"/2/users/show.json",
     *      "error_code":10006,
     *      "error":"source paramter(appkey) is missing"
     *   }
     * }
     * 可以通过error_code 判断是否正确
     * @param access_token
     * @param uid
     * @return
     */
    public Map userInfo(String access_token, Integer uid) {
        AssertUtil.hasText(access_token,"access_token为空");
        AssertUtil.isTrue(Objects.nonNull(uid),"uid为空");

        Map map = HttpClientUtils.getInstance().
                putParams("access_token", access_token).
                putParams("uid", uid).
                doGet("https://api.weibo.com/2/users/show.json").
                toMap();
        return map;
    }

    public Integer getUid() {
        return uid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public Date getExpireTime() {
        return expireTime;
    }
}
