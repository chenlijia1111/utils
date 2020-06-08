package com.github.chenlijia1111.util.oauth;

import com.github.chenlijia1111.utils.encrypt.SHA1EncryptUtil;
import com.github.chenlijia1111.utils.http.URLBuildUtil;
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

    @Test
    public void test2() {
        //jsapi_ticket=sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg&
        // noncestr=Wm3WZYTPz0wzccnW&
        // timestamp=1414587457&
        // url=http://mp.weixin.qq.com?params=value
        String noncestr = "Wm3WZYTPz0wzccnW";
        String timeStamp = "1414587457";
        StringBuilder sb = new StringBuilder();
        URLBuildUtil urlBuildUtil = new URLBuildUtil(null);
        String paramsToString = urlBuildUtil.
                putParams("noncestr", noncestr).
                putParams("jsapi_ticket", "sM4AOVdWfPE4DxkXGEs8VMCPGGVi4C3VM0P37wVUCFvkVAy_90u5h9nbSlYy3-Sl-HhTdfl2fzFy1AOcHKP7qg").
                putParams("timestamp", timeStamp).
                putParams("url", "http://mp.weixin.qq.com?params=value").paramsToString();

        System.out.println(paramsToString);

        //sha1签名
        String signature = SHA1EncryptUtil.SHA1StringToHexString(paramsToString).toLowerCase();
        System.out.println(signature);
    }

}
