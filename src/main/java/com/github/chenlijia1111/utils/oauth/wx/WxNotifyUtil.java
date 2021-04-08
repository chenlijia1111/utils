package com.github.chenlijia1111.utils.oauth.wx;

import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.http.HttpClientUtils;
import com.github.chenlijia1111.utils.list.Maps;
import com.github.chenlijia1111.utils.list.annos.MapType;
import org.apache.http.entity.ContentType;
import org.apache.http.protocol.HTTP;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 微信模板通知工具
 * 未测试
 *
 * @author Chen LiJia
 * @since 2021/2/7
 */
public class WxNotifyUtil {

    // 模板id 是通用参数，不管是小程序还是公众号都要传的
    private String templeteId;

    //-----小程序消息参数---
    private String accessToken;

    private String openId;

    private String page;

    /**
     * 这个 formid 是需要用户主动订阅才可以拿到这个 formid 的
     * 获取方式为在前端写一个表单，然后表单提交的时候就可以获取到一个 formId
     * 利用这个 formId 就可以拿来发送订阅消息了
     */
    private String formId;

    //--------------公众号消息参数------------

    private String appId;

    // 公众号的路径
    private String url;

    /**
     * 跳转到小程序的参数
     * 点击公众号的通知可以跳转到小程序
     */
    private Map naviateAppleteParams;

    //----------订阅消息内容------
    private Map data = new HashMap();

    /**
     * 添加通知内容
     *
     * @param key
     * @param value
     * @param color 这个颜色是公众号的通知的参数 小程序不需要传
     * @return
     */
    public WxNotifyUtil addNotifyContent(String key, String value, String color) {
        if (StringUtils.isNotEmpty(key) && StringUtils.isNotEmpty(value)) {
            Maps.MapBuilder valueMapBuilder = Maps.mapBuilder(MapType.HASH_MAP).put("value", value);
            if (StringUtils.isNotEmpty(color)) {
                valueMapBuilder.put("color", color);
            }
            Map valueMap = valueMapBuilder.build();
            this.data.put(key, valueMap);
        }
        return this;
    }

    /**
     * 设置公众号通知
     * 跳转到小程序的信息
     * 设置了之后点击公众号的通知可以跳转到对应的小程序页面
     *
     * @param appId
     * @param pagePath
     * @return
     */
    private WxNotifyUtil addNaviateAppleteParams(String appId, String pagePath) {
        if (StringUtils.isNotEmpty(appId) && StringUtils.isNotEmpty(pagePath)) {
            this.naviateAppleteParams = Maps.mapBuilder(MapType.HASH_MAP).
                    put("appid", appId).put("pagepath", pagePath).build();
        }
        return this;
    }

    public WxNotifyUtil setTempleteId(String templeteId) {
        this.templeteId = templeteId;
        return this;
    }

    public WxNotifyUtil setAccessToken(String accessToken) {
        this.accessToken = accessToken;
        return this;
    }

    public WxNotifyUtil setOpenId(String openId) {
        this.openId = openId;
        return this;
    }

    public WxNotifyUtil setPage(String page) {
        this.page = page;
        return this;
    }

    public WxNotifyUtil setFormId(String formId) {
        this.formId = formId;
        return this;
    }

    public WxNotifyUtil setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public WxNotifyUtil setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * 发送小程序模板消息
     * {
     * "touser":"OPENID",
     * "weapp_template_msg":{
     * "template_id":"TEMPLATE_ID",
     * "page":"page/page/index",
     * "form_id":"FORMID",
     * "data":{
     * "keyword1":{
     * "value":"339208499"
     * },
     * "keyword2":{
     * "value":"2015年01月05日 12:30"
     * },
     * "keyword3":{
     * "value":"腾讯微信总部"
     * },
     * "keyword4":{
     * "value":"广州市海珠区新港中路397号"
     * }
     * },
     * "emphasis_keyword":"keyword1.DATA"
     * },
     * "mp_template_msg":{
     * "appid":"APPID ",
     * "template_id":"TEMPLATE_ID",
     * "url":"http://weixin.qq.com/download",
     * "miniprogram":{
     * "appid":"xiaochengxuappid12345",
     * "pagepath":"index?foo=bar"
     * },
     * "data":{
     * "first":{
     * "value":"恭喜你购买成功！",
     * "color":"#173177"
     * },
     * "keyword1":{
     * "value":"巧克力",
     * "color":"#173177"
     * },
     * "keyword2":{
     * "value":"39.8元",
     * "color":"#173177"
     * },
     * "keyword3":{
     * "value":"2014年9月22日",
     * "color":"#173177"
     * },
     * "remark":{
     * "value":"欢迎再次购买！",
     * "color":"#173177"
     * }
     * }
     * }
     * }
     *
     * @return
     */
    public Result sendAppletNotify() {
        if (StringUtils.isEmpty(this.accessToken)) {
            return Result.failure("accessToken 为空");
        }
        if (StringUtils.isEmpty(this.templeteId)) {
            return Result.failure("模板id 为空");
        }
        if (StringUtils.isEmpty(this.formId)) {
            return Result.failure("formId 为空");
        }
        if (this.data.size() == 0) {
            return Result.failure("消息内容为空");
        }
        if (StringUtils.isEmpty(this.page)) {
            return Result.failure("page 为空");
        }

        // emphasis_keyword 为 小程序模板放大关键词
        // 就直接取 data 里的第一个数据就行了
        Collection values = this.data.values();
        String emphasisKeyword = values.iterator().next().toString();

        // 可以请求
        // 构造请求参数
        Map params = Maps.mapBuilder(MapType.HASH_MAP).
                put("touser", openId).
                put("weapp_template_msg", Maps.mapBuilder(MapType.HASH_MAP).
                        put("template_id", this.templeteId).
                        put("page", this.page).
                        put("form_id", this.formId).
                        put("data", this.data).
                        put("emphasis_keyword", emphasisKeyword).
                        build()).
                build();
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/uniform_send?access_token=" + this.accessToken;
        Map map = HttpClientUtils.getInstance().
                putParams(params).
                putHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()).
                doPost(url).toMap();
        //{
        // "errcode": 0,
        // "errmsg": "ok"
        //}
        if (Objects.isNull(map)) {
            return Result.failure("操作失败");
        }
        if (Objects.nonNull(map) && !Objects.equals("0", map.get("errcode"))) {
            return Result.failure(map.get("errmsg").toString());
        }
        return Result.success();
    }

    /**
     * 发送公众号通知
     * @return
     */
    public Result sendJsNotify() {
        if (StringUtils.isEmpty(this.accessToken)) {
            return Result.failure("accessToken 为空");
        }
        if (StringUtils.isEmpty(this.appId)) {
            return Result.failure("appId 为空");
        }
        if (StringUtils.isEmpty(this.templeteId)) {
            return Result.failure("模板id 为空");
        }
        if (StringUtils.isEmpty(this.url)) {
            return Result.failure("url 为空");
        }
        if (this.data.size() == 0) {
            return Result.failure("消息内容为空");
        }
        if (StringUtils.isEmpty(this.page)) {
            return Result.failure("page 为空");
        }

        // emphasis_keyword 为 小程序模板放大关键词
        // 就直接取 data 里的第一个数据就行了
        Collection values = this.data.values();
        String emphasisKeyword = values.iterator().next().toString();

        // 可以请求
        // 构造请求参数
        Map params = Maps.mapBuilder(MapType.HASH_MAP).
                put("touser", openId).
                put("weapp_template_msg", Maps.mapBuilder(MapType.HASH_MAP).
                        put("template_id", this.templeteId).
                        put("page", this.page).
                        put("form_id", this.formId).
                        put("data", this.data).
                        put("emphasis_keyword", emphasisKeyword).
                        build()).
                build();
        String url = "https://api.weixin.qq.com/cgi-bin/message/wxopen/template/uniform_send?access_token=" + this.accessToken;
        Map map = HttpClientUtils.getInstance().
                putParams(params).
                putHeader(HTTP.CONTENT_TYPE, ContentType.APPLICATION_JSON.getMimeType()).
                doPost(url).toMap();
        //{
        // "errcode": 0,
        // "errmsg": "ok"
        //}
        if (Objects.isNull(map)) {
            return Result.failure("操作失败");
        }
        if (Objects.nonNull(map) && !Objects.equals("0", map.get("errcode"))) {
            return Result.failure(map.get("errmsg").toString());
        }
        return Result.success();
    }


}
