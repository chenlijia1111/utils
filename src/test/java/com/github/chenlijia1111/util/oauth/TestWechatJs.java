package com.github.chenlijia1111.util.oauth;

import com.github.chenlijia1111.utils.oauth.wx.WXJsUtil;
import org.junit.Test;

/**
 * @author Chen LiJia
 * @since 2020/6/3
 */
public class TestWechatJs {

    @Test
    public void test1(){
        WXJsUtil wxJsUtil = new WXJsUtil("wx7d87c77c4c8f78da", "14f8731abca5e6c063e339cdcbb9a9c8");
        String token = wxJsUtil.accessToken("wx7d87c77c4c8f78da", "14f8731abca5e6c063e339cdcbb9a9c8");
        System.out.println(token);
        System.out.println("ticket:"+wxJsUtil.getTicket());
    }

}
