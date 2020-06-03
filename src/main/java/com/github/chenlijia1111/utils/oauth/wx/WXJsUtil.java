package com.github.chenlijia1111.utils.oauth.wx;

import com.github.chenlijia1111.utils.core.RandomUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.encrypt.SHA1EncryptUtil;
import com.github.chenlijia1111.utils.http.HttpClientUtils;
import com.github.chenlijia1111.utils.http.URLBuildUtil;
import org.joda.time.DateTime;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * 微信公众号工具类
 * 注意，这个是公众号的accessToken 跟普通的那个不一样，这个是用来获取jsApiTicket的
 * 公众号的accessToken是对应整个应用的，而不是对应单个用户
 * 所以这个accessToken要全局缓存起来
 * 最好单例当前工具类
 *
 * @author Chen LiJia
 * @since 2020/6/3
 */
public class WXJsUtil {

    private String appId;

    private String secret;

    private String accessToken;

    private Date limitTime;

    private String jsApiTicket;

    private Date ticketLimitTime;

    public WXJsUtil(String appId, String secret) {
        this.appId = appId;
        this.secret = secret;
        accessToken(appId, secret);
    }

    /**
     * 获取accessToken
     * GET https://api.weixin.qq.com/cgi-bin/token
     * <p>
     * 正确返回信息
     * {@code
     * {"access_token":"ACCESS_TOKEN","expires_in":7200}
     * }
     * 错误返回信息
     * {@code
     * {"errcode":40013,"errmsg":"invalid appid"}
     * }
     *
     * @param appId
     * @param secret
     * @return
     */
    public String accessToken(String appId, String secret) {

        this.appId = appId;
        this.secret = secret;

        //判断是否之前有获取到
        if (StringUtils.isNotEmpty(accessToken) && Objects.nonNull(limitTime) &&
                limitTime.getTime() > System.currentTimeMillis()) {
            return accessToken;
        }

        Map map = HttpClientUtils.getInstance().putParams("grant_type", "client_credential").
                putParams("appid", appId).putParams("secret", secret).
                doGet("https://api.weixin.qq.com/cgi-bin/token").toMap();
        if (Objects.nonNull(map) && !map.containsKey("errcode")) {
            Object access_token = map.get("access_token");
            Object expires_in = map.get("expires_in");
            this.accessToken = access_token.toString();
            Integer expireSecond = (Integer) expires_in;
            this.limitTime = DateTime.now().plusSeconds(expireSecond).toDate();
            return this.accessToken;
        }
        return null;
    }

    /**
     * https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
     *
     * @return
     */
    public String getTicket() {
        if (StringUtils.isNotEmpty(accessToken)) {

            //判断之前有没有获取过
            if (StringUtils.isNotEmpty(jsApiTicket) && Objects.nonNull(ticketLimitTime) &&
                    ticketLimitTime.getTime() > System.currentTimeMillis()) {
                return jsApiTicket;
            }


            //有accessToken
            //判断有没有过期，过期就重新获取
            accessToken(this.appId, this.secret);

            Map map = HttpClientUtils.getInstance().
                    putParams("access_token", this.accessToken).
                    putParams("type", "jsapi").
                    doGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket").
                    toMap();
            if (Objects.nonNull(map) && Objects.equals(map.get("errcode"), 0)) {
                Object ticket = map.get("ticket");
                Object expires_in = map.get("expires_in");
                this.jsApiTicket = ticket.toString();
                Integer expireSecond = (Integer) expires_in;
                this.ticketLimitTime = DateTime.now().plusSeconds(expireSecond).toDate();
                return this.jsApiTicket;
            }
        }
        return null;
    }


    /**
     * 构建签名信息
     * 前置信息，需要先获取accessToken之后才可以获取
     *
     * @param url 当前网页的URL，不包含#及其后面部分
     * @return
     */
    public Map createSignatureInfo(String url) {

        String noncestr = RandomUtil.createUUID();
        String timeStamp = String.valueOf(System.currentTimeMillis() / 1000);
        StringBuilder sb = new StringBuilder();
        URLBuildUtil urlBuildUtil = new URLBuildUtil(null);
        String paramsToString = urlBuildUtil.
                putParams("noncestr", noncestr).
                putParams("jsapi_ticket", getTicket()).
                putParams("timestamp", timeStamp).
                putParams("url", url).paramsToString();

        //sha1签名
        String signature = SHA1EncryptUtil.SHA1StringToHexString(paramsToString).toLowerCase();

        urlBuildUtil.putParams("signature", signature);
        return urlBuildUtil.getParams();
    }

}
