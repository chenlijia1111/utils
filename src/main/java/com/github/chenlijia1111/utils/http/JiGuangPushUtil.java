package com.github.chenlijia1111.utils.http;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.common.constant.ContentTypeConstant;
import com.github.chenlijia1111.utils.core.enums.CharSetType;
import com.github.chenlijia1111.utils.list.Lists;
import org.apache.http.protocol.HTTP;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 极光推送工具类
 * <p>
 * 请求参数
 * {@code {
 * "cid": "8103a4c628a0b98974ec1949-711261d4-5f17-4d2f-a855-5e5a8909b26e",
 * "platform": "all",
 * "audience": {
 * "tag": [
 * "深圳",
 * "北京"
 * ]
 * },
 * "notification": {
 * "android": {
 * "alert": "Hi, JPush!",
 * "title": "Send to Android",
 * "builder_id": 1,
 * "large_icon": "http://www.jiguang.cn/largeIcon.jpg",
 * "intent": {
 * "url": "intent:#Intent;component=com.jiguang.push/com.example.jpushdemo.SettingActivity;end",
 * },
 * "extras": {
 * "newsid": 321
 * }
 * },
 * "ios": {
 * "alert": "Hi, JPush!",
 * "sound": "default",
 * "badge": "+1",
 * "thread-id": "default"
 * "extras": {
 * "newsid": 321
 * }
 * },
 * "voip": {    // 此功能需要 JPush iOS SDK v3.3.2 及以上版本支持
 * "key": "value" // 任意自定义key/value对，api透传下去
 * }
 * },
 * "message": {
 * "msg_content": "Hi,JPush",
 * "content_type": "text",
 * "title": "msg",
 * "extras": {
 * "key": "value"
 * }
 * },
 * "sms_message":{
 * "temp_id":1250,
 * "temp_para":{
 * "code":"123456"
 * },
 * "delay_time":3600,
 * "active_filter":false
 * },
 * "options": {
 * "time_to_live": 60,
 * "apns_production": false,
 * "apns_collapse_id":"jiguang_test_201706011100"
 * },
 * "callback": {
 * "url":"http://www.bilibili.com",
 * "params":{
 * "name":"joe",
 * "age":26
 * },
 * "type":3
 * }
 * }
 * }
 * <p>
 * <p>
 * 测试代码如下：
 * {@code
 * //进行推送消息  逻小维：一会要一起出门玩么？  客户端注意 要截取第一个冒号之后的数据进行展示，
 * String messageContent = sendUserNickName + "：" + params.getMessageContent();
 * //推送目标对象构建
 * Map audience = Maps.mapBuilder(MapType.HASH_MAP).put("alias", aliasNameSet).build();
 * //通知内容 安卓发送通知时添加通知栏大图标 为应用图标
 * String requestPrefixUrl = HttpUtils.currentRequestUrlPrefix(SpringUtil.getCurrentRequest());
 * String bzPushIconUrl = requestPrefixUrl + "/images/icons/bz_push_icon.png";
 * Map androidNotification = Maps.mapBuilder(MapType.HASH_MAP).
 * put("alert", messageContent).
 * put("extras",params).
 * put("large_icon",bzPushIconUrl).
 * build();
 * Map iosNotification = Maps.mapBuilder(MapType.HASH_MAP).
 * put("alert", messageContent).
 * put("extras",params).
 * build();
 * Map notification = Maps.mapBuilder(MapType.HASH_MAP).
 * put("android", androidNotification).
 * put("ios", iosNotification).
 * build();
 * //True 表示推送生产环境，False 表示要推送开发环境
 * Map options = Maps.mapBuilder(MapType.HASH_MAP).
 * put("apns_production", false).
 * build();
 * Map map = new JiGuangPushUtil(Constants.PUSH_APP_KEY, Constants.PUSH_APP_SECRET).
 * platform("all").
 * audience(audience).
 * notification(notification).
 * options(options).
 * push();
 * }
 *
 * @author Chen LiJia
 * @since 2020/3/28
 */
public class JiGuangPushUtil {


    //请求地址
    private String baseUrl = "https://api.jpush.cn/v3/push";

    private String appKey;

    private String masterSecret;

    //请求工具类
    private HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();

    public JiGuangPushUtil(String appKey, String masterSecret) {

        AssertUtil.hasText(appKey, "appKey不能为空");
        AssertUtil.hasText(masterSecret, "masterSecret不能为空");

        this.appKey = appKey;
        this.masterSecret = masterSecret;
    }

    /**
     * 设置推送平台
     * {@code { "platform" : "all" }}
     * 或者
     * {@code { "platform" : ["android", "ios"] }}
     *
     * @param platform
     * @return
     */
    public JiGuangPushUtil platform(String... platform) {
        //允许推送的平台参数
        List<String> allowPlatFormList = Lists.asList("all", "android", "ios", "winphone");
        AssertUtil.isTrue(null != platform && platform.length > 0 &&
                !Lists.asList(platform).stream().filter(e -> !allowPlatFormList.contains(e)).findAny().isPresent(), "推送平台不合法");

        List<String> platformList = Lists.asList(platform);
        if (platformList.stream().filter(e -> Objects.equals(e, "all")).findAny().isPresent()) {
            httpClientUtils.putParams("platform", "all");
        } else {
            httpClientUtils.putParams("platform", platformList);
        }
        return this;
    }


    /**
     * 设置推送目标，自己构建对象，对象形式如下
     * 推送给全部广播 {@code {"audience" : "all"}}
     * 推送给多个标签 {@code {
     * "audience" : {
     * "tag" : [ "深圳", "广州", "北京" ]
     * }
     * }}
     * 文档 http://docs.jiguang.cn/jpush/server/push/rest_api_v3_push/#audience
     *
     * @param audience
     * @return
     */
    public JiGuangPushUtil audience(Object audience) {
        if (Objects.nonNull(audience)) {
            httpClientUtils.putParams("audience", audience);
        }
        return this;
    }

    /**
     * 通知
     * 文档 http://docs.jiguang.cn/jpush/server/push/rest_api_v3_push/#notification
     * {@code {
     * "notification" : {
     * "android" : {
     * "alert" : "hello, JPush!",
     * "title" : "JPush test",
     * "builder_id" : 3,
     * "style":1  // 1,2,3
     * "alert_type":1 // -1 ~ 7
     * "big_text":"big text content",
     * "inbox":JSONObject,
     * "big_pic_path":"picture url",
     * "priority":0, // -2~2
     * "category":"category str",
     * "large_icon": "http://www.jiguang.cn/largeIcon.jpg",
     * "intent": {
     * "url": "intent:#Intent;component=com.jiguang.push/com.example.jpushdemo.SettingActivity;end",
     * },
     * "extras" : {
     * "news_id" : 134,
     * "my_key" : "a value"
     * }
     * }
     * }
     * }}
     *
     * @param notification
     * @return
     */
    public JiGuangPushUtil notification(Object notification) {
        if (Objects.nonNull(notification)) {
            httpClientUtils.putParams("notification", notification);
        }
        return this;
    }

    /**
     * 自定义消息
     * 此部分内容不会展示到通知栏上，JPush SDK 收到消息内容后透传给 App。需要 App 自行处理。
     * iOS 在推送应用内消息通道（非 APNS）获取此部分内容，即需 App 处于前台。Windows Phone 暂时不支持应用内消息。
     *
     * @param message
     * @return
     */
    public JiGuangPushUtil message(Object message) {
        if (Objects.nonNull(message)) {
            httpClientUtils.putParams("message", message);
        }
        return this;
    }

    /**
     * 可选参数
     *
     * @param options
     * @return
     */
    public JiGuangPushUtil options(Object options) {
        if (Objects.nonNull(options)) {
            httpClientUtils.putParams("options", options);
        }
        return this;
    }

    /**
     * 发起推送
     *
     * @return
     */
    public Map push() {

        //构建 token
        String token = Base64.getEncoder().encodeToString((this.appKey + ":" + this.masterSecret).getBytes(Charset.forName(CharSetType.UTF8.name())));
        httpClientUtils.putHeader("Authorization", "Basic " + token);
        httpClientUtils.putHeader(HTTP.CONTENT_TYPE, ContentTypeConstant.APPLICATION_JSON);

        //校验必填参数
        AssertUtil.isTrue(Objects.nonNull(httpClientUtils.getParams().get("platform")), "推送平台设置不能为空");
        AssertUtil.isTrue(Objects.nonNull(httpClientUtils.getParams().get("audience")), "推送设备指定不能为空");
        AssertUtil.isTrue(Objects.nonNull(httpClientUtils.getParams().get("notification")) ||
                Objects.nonNull(httpClientUtils.getParams().get("message")), "通知内容体 与 消息内容体 二选一必须有一个");

        Map map = httpClientUtils.doPost(this.baseUrl).toMap();
        return map;
    }

    /**
     * 获取请求参数
     * @return
     */
    public Map getQueryParams() {
        return httpClientUtils.getParams();
    }
}
