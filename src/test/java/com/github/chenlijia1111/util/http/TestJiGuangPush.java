package com.github.chenlijia1111.util.http;

import com.github.chenlijia1111.utils.http.HttpClientUtils;
import com.github.chenlijia1111.utils.http.JiGuangPushUtil;
import com.github.chenlijia1111.utils.list.Maps;
import com.github.chenlijia1111.utils.list.annos.MapType;
import org.junit.Test;

import java.util.List;
import java.util.Map;

/**
 * 测试极光推送
 *
 * 推送对象参数
 * {@code {
 *     "cid": "8103a4c628a0b98974ec1949-711261d4-5f17-4d2f-a855-5e5a8909b26e",
 *     "platform": "all",
 *     "audience": {
 *         "tag": [
 *             "深圳",
 *             "北京"
 *         ]
 *     },
 *     "notification": {
 *         "android": {
 *             "alert": "Hi, JPush!",
 *             "title": "Send to Android",
 *             "builder_id": 1,
 *             "large_icon": "http://www.jiguang.cn/largeIcon.jpg",
 *             "intent": {
 *                 "url": "intent:#Intent;component=com.jiguang.push/com.example.jpushdemo.SettingActivity;end",
 *             },
 *             "extras": {
 *                 "newsid": 321
 *             }
 *         },
 *         "ios": {
 *             "alert": "Hi, JPush!",
 *             "sound": "default",
 *             "badge": "+1",
 *             "thread-id": "default"
 *             "extras": {
 *                 "newsid": 321
 *             }
 *         },
 *         "voip": {    // 此功能需要 JPush iOS SDK v3.3.2 及以上版本支持
 *             "key": "value" // 任意自定义key/value对，api透传下去
 *         }
 *     },
 *     "message": {
 *         "msg_content": "Hi,JPush",
 *         "content_type": "text",
 *         "title": "msg",
 *         "extras": {
 *             "key": "value"
 *         }
 *     },
 *     "sms_message":{
 *        "temp_id":1250,
 *        "temp_para":{
 *             "code":"123456"
 *        },
 *         "delay_time":3600,
 *         "active_filter":false
 *     },
 *     "options": {
 *         "time_to_live": 60,
 *         "apns_production": false,
 *         "apns_collapse_id":"jiguang_test_201706011100"
 *     },
 *     "callback": {
 *         "url":"http://www.bilibili.com",
 *         "params":{
 *             "name":"joe",
 *             "age":26
 *          },
 *          "type":3
 *     }
 * }}
 *
 * @author Chen LiJia
 * @since 2020/3/28
 */
public class TestJiGuangPush {

    @Test
    public void test1(){
        Map map = Maps.mapBuilder(MapType.HASH_MAP).put("alert", "哈哈哈").build();
        Map all = new JiGuangPushUtil("123", "23423").platform("all").audience("all").notification(map).push();
        System.out.println(all);
    }



}
