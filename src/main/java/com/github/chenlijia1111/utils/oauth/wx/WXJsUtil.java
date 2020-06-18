package com.github.chenlijia1111.utils.oauth.wx;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.LogUtil;
import com.github.chenlijia1111.utils.core.RandomUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.encrypt.SHA1EncryptUtil;
import com.github.chenlijia1111.utils.http.HttpClientUtils;
import com.github.chenlijia1111.utils.http.URLBuildUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.TreeSet;
import java.util.stream.Collectors;

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

    private static final Logger log = new LogUtil(WXJsUtil.class);

    private String appId;

    private String secret;

    private String accessToken;

    private Date limitTime;

    private String jsApiTicket;

    private Date ticketLimitTime;

    public WXJsUtil(String appId, String secret) {

        AssertUtil.hasText(appId,"appId不能为空");
        AssertUtil.hasText(secret,"secret不能为空");

        this.appId = appId;
        this.secret = secret;
        accessToken(appId, secret);
    }


    /**
     * 验证消息的确来自微信服务器
     * 若确认此次GET请求来自微信服务器，请原样返回echostr参数内容
     *
     * @param signature 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数。
     * @param timestamp 时间戳
     * @param nonce     随机数
     * @param echostr   随机字符串 校验成功之后返回微信这个参数内容
     * @param token     微信公众号设置的token
     * @param response
     */
    public void checkSignature(String signature, String timestamp, String nonce,
                               String echostr, String token, HttpServletResponse response) {
        if (StringUtils.isNotEmpty(signature) && StringUtils.isNotEmpty(timestamp) &&
                StringUtils.isNotEmpty(nonce) && StringUtils.isNotEmpty(echostr) &&
                StringUtils.isNotEmpty(token) && Objects.nonNull(response)) {

            log.info("signature:{},timestamp:{},nonce:{},echostr:{}", signature, timestamp, nonce, echostr);

            //校验
            TreeSet<String> treeSet = new TreeSet<>();
            treeSet.add(token);
            treeSet.add(timestamp);
            treeSet.add(nonce);
            String tempStr = treeSet.stream().collect(Collectors.joining());
            //sha1加密
            tempStr = SHA1EncryptUtil.SHA1StringToHexString(tempStr).toLowerCase();
            //比较是否一致
            if (Objects.equals(tempStr, signature)) {
                //说明是微信发来的
                try {
                    log.info("校验微信发来的消息通过");
                    response.getWriter().print(echostr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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
        //打印错误信息
        log.error(map.get("errmsg").toString());
        return null;
    }

    /**
     * https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi
     *
     * @return
     */
    public String getTicket() {

        //刷新accessToken
        accessToken(this.appId,this.secret);

        if (StringUtils.isNotEmpty(accessToken)) {

            //判断之前有没有获取过
            if (StringUtils.isNotEmpty(jsApiTicket) && Objects.nonNull(ticketLimitTime) &&
                    ticketLimitTime.getTime() > System.currentTimeMillis()) {
                return jsApiTicket;
            }

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

            //打印错误信息
            log.error(map.get("errmsg").toString());
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
        String ticket = getTicket();
        //获取不到jsTicket 可能是ip没有设置白名单的问题
        if (StringUtils.isNotEmpty(ticket)) {
            String paramsToString = urlBuildUtil.
                    putParams("noncestr", noncestr).
                    putParams("jsapi_ticket", getTicket()).
                    putParams("timestamp", timeStamp).
                    putParams("url", url).paramsToString();

            //sha1签名
            String signature = SHA1EncryptUtil.SHA1StringToHexString(paramsToString).toLowerCase();

            urlBuildUtil.putParams("signature", signature);
            urlBuildUtil.putParams("appId", this.appId);
            return urlBuildUtil.getParams();
        }
        return null;
    }

}
