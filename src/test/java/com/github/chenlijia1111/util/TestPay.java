package com.github.chenlijia1111.util;

import com.github.chenlijia1111.utils.pay.wx.PayType;
import com.github.chenlijia1111.utils.pay.wx.WXPayUtil;
import org.junit.Test;

import java.util.Map;

/**
 * @author 陈礼佳
 * @since 2019/9/15 16:02
 */
public class TestPay {

    @Test
    public void test1() {
        Map preOrder = WXPayUtil.createPreOrder("wxd3fdb22474bdd195",
                "1553017211", "商品1",
                "XfenDefeEYdtv7jHrWx0es2wJMTI7T7i", 100,
                "https://www.shixianly.cn/system", PayType.APP, "sa", null);
        System.out.println(preOrder);
    }
}
