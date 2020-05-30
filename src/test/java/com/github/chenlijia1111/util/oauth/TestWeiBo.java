package com.github.chenlijia1111.util.oauth;

import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.oauth.weibo.WeiBoLoginUtil;
import org.junit.Test;

import java.util.Map;

/**
 * @author Chen LiJia
 * @since 2020/5/30
 */
public class TestWeiBo {

    @Test
    public void testAccessToken(){
        Map map = WeiBoLoginUtil.accessToken("195395370", "7f2b359fc44e44811ed58cdfdd5b8024", "12", "121");
        System.out.println(JSONUtil.objToStr(map));
    }

    @Test
    public void testCheckAccessToken(){
        Map map = WeiBoLoginUtil.getTokenInfo("195395370");
        System.out.println(JSONUtil.objToStr(map));
    }

    @Test
    public void testUserInfo(){
        Map map = WeiBoLoginUtil.userInfo("195395370",12);
        System.out.println(JSONUtil.objToStr(map));
    }

}
