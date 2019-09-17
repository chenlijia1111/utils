package com.github.chenlijia1111.util;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayConstants;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.AlipayTradeAppPayModel;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chenlijia1111.utils.common.constant.TimeConstant;
import com.github.chenlijia1111.utils.core.RandomUtil;
import com.github.chenlijia1111.utils.core.enums.CharSetType;
import com.github.chenlijia1111.utils.encrypt.RSAUtils;
import com.github.chenlijia1111.utils.encrypt.enums.SignType;
import com.github.chenlijia1111.utils.http.HttpClientUtils;
import com.github.chenlijia1111.utils.pay.wx.PayType;
import com.github.chenlijia1111.utils.pay.wx.WXPayUtil;
import org.joda.time.DateTime;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
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


    /**
     * 支付宝需要在传入参数的时候就需要对中文做处理
     * 而且不可以处理那些={}等符号，只需要处理掉中文即可
     *
     * @throws AlipayApiException
     */
    @Test
    public void test2() throws AlipayApiException, UnsupportedEncodingException {
        String appId = "2016090800462923";
        String privateKey = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCDljrRc0prjlLEomDOpUL3Dsddr6VpzSb76Q5J1VJmh7pM2hmiK3/eOBhayjsoJ1ZvSxHh10o6+2FhzXhD4tYLHmKv+z5TswuD05Bm/4KbkPEe+hssoZlNR9fvv49RsAYFUNTwHKEzBUnOaN1NY/BOJgPXIapfoCV2JcUbAk/UhSzYGIVX0DFMjUCuyhaOC+/vVZIZK8EDjRq8m/Vt08gtuqR8elQoRsNWaknQqG1iv8uk5m1K0wrmcKQORqQDdgGfeSL2rEIG5N6xdm7o4haLJjDXnQ8YE6J9Rb7/7uTQLSJMAeg9tUeQe1pci47NHMQRBlk/L3IufCUjjViutzL9AgMBAAECggEANL3WPXVUct/M1PuwiaM7wAKwbiCk0E9mPLog+/8A1A+64Gras2F0EwGw4VMFewapYlpYhIjiO9neInFUrwEQgcxgCeFODgZJq0D1NBNdqjTiMGRW2AC9jXgqWPIS488F2sOXVdZj1itcddsaOJbCDtC14VihDUsHON/SrQy0Kp3Pd7S6NBNsjVuZ2h0Qe35+6ZBJ/0ZE32D/c1UGkh+STnO184R8Dg5T+8R/cchlH1n+zETdpmRYvcjoSjD523kF95lFSBN3QFySz8GuIMAuQEvYXZrEehqjJSmUwmjV6e9eHn0QXSCtyskJ3eSHrEE7JRYNZ1K5JzoCY6ZxeLynkQKBgQDmThXbwaopAz0oQx73TpxbHaVZsGkY5/Sd9GnNZeUTYQyNDiikkY3R0cMQ0niFssgY2ROzhE36BfsQ5aMOqyoS3iMsG/ZmdzjMF9hRyebx3dkLXnbDZx6HSJIiRYGoLRSnhgSgLngmcFEe7/Bax5nOYY+BXARiASl50U90FN3P4wKBgQCSRJSEd5H4N8TlQimOk8P+nikwMCee5EzBWXrrL+htAzKDG+K+unJJZ2lTXK7X+tK4xwgTDaTYaLAlUGOctVIJ9YqLIbrQpdDrYVJaEQCwRQp6tugjBhDFmgLEfaMRpqtnu1DpJ2/bRhFQ1hIB68YsAT0L6n7pGOdzZ1zBpDKnnwKBgQDarFve3EUIShYAq7K0WUTm0wBUZ8dyqZO/gvoO1+gpfWWgbUgz8ix5hDU2PNHJCNcrmLo3IrGza4hXI+OuIuGBJsC4uhg8dIlOaHmyNwX8s4vJWrljkf8Prt1qgxq4J43L/tlXiqf0v4KW4HYUjB9DMalDx641rCrIivBpJQLzxwKBgGup72hSWy/KXTQjr1MzkWEqKkc+KuvaQGVR6BiV+w/C+rtnG1ApDVbat7yUzF5OUi2Q03Cy6lQhOwc8VRZUoT6TbpdbDJfodg/MY0HIoikdqMkLOXQOUuogo0je8dlcRW7lgnkcfkl5GeXWrQqc8hISF01eHmyV2EgqHSysIF0hAoGAL5L3OZdHQTKeMde8Zd7ZiuwlCkr5NQ+HpCqB3IcJRHQbyFk6tP8TGDMxXhsKO8Q/JklUf29sjou1nsbFmCebyrpwLUebgFh2YIwecBmY2t855/MWbUb//YLlq1shmedDYhl8CM/P9vEp72sFpFRlIoetw2C1k1zXLIOF7f+oyy0=";
        String publicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAg5Y60XNKa45SxKJgzqVC9w7HXa+lac0m++kOSdVSZoe6TNoZoit/3jgYWso7KCdWb0sR4ddKOvthYc14Q+LWCx5ir/s+U7MLg9OQZv+Cm5DxHvobLKGZTUfX77+PUbAGBVDU8ByhMwVJzmjdTWPwTiYD1yGqX6AldiXFGwJP1IUs2BiFV9AxTI1ArsoWjgvv71WSGSvBA40avJv1bdPILbqkfHpUKEbDVmpJ0KhtYr/LpOZtStMK5nCkDkakA3YBn3ki9qxCBuTesXZu6OIWiyYw150PGBOifUW+/+7k0C0iTAHoPbVHkHtaXIuOzRzEEQZZPy9yLnwlI41Yrrcy/QIDAQAB";
        String serverUrl = "https://openapi.alipaydev.com/gateway.do";
        String domain = "https://www.shixianly.cn/system";



        HttpClientUtils instance = HttpClientUtils.getInstance();
        instance.putParams("app_id", appId);
        instance.putParams("method", "alipay.trade.app.pay");
        instance.putParams("format", "JSON");
        instance.putParams("charset", "utf-8");
        instance.putParams("sign_type", "RSA2");
        instance.putParams("timestamp", DateTime.now().toString(TimeConstant.DATE_TIME));
        instance.putParams("version", "1.0");
        instance.putParams("app_id", appId);

        HashMap<String, String> bizContentMap = new HashMap<>();
        bizContentMap.put("total_amount", "9.00");
        bizContentMap.put("subject", "daletou");
        bizContentMap.put("out_trade_no", RandomUtil.createUUID());

        try {
            String string = new ObjectMapper().writeValueAsString(bizContentMap);
            instance.putParams("biz_content", string);

            String dStr = instance.paramsToString(true);

            System.out.println(dStr);

            String sign1 = RSAUtils.sign(privateKey,
                    dStr, SignType.SHA256_WITH_RSA);
            System.out.println(sign1);
            boolean b = RSAUtils.checkSign(publicKey, dStr,
                    sign1, SignType.SHA256_WITH_RSA);
            System.out.println(b);

            String s = AlipaySignature.rsaSign(dStr,
                    privateKey, CharSetType.UTF8.getType(), AlipayConstants.SIGN_TYPE_RSA2);
            System.out.println(s);

            //构建签名
            String string1 = instance.paramsToString(true);
            String sign = RSAUtils.sign(privateKey, string1, SignType.SHA256_WITH_RSA);
            instance.putParams("sign", sign);

            //请求
            Map map = instance.doGet2(serverUrl);
            System.out.println(map);



        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }


    @Test
    public void test3() {
        Map map = HttpClientUtils.getInstance().putParams("userNum", "admin").putParams("password", "123456").
                doPost("http://58.250.17.31:8334/expertise/api/user/login");
        System.out.println(map);


        Map map1 = HttpClientUtils.getInstance().putParams("page", "1").putParams("limit", "10").
                putParams("userName", "管理员").
                putHeader("authentication", "e639514a859144fb814752063036577b").
                doGet("http://58.250.17.31:8334/expertise/api/user/listAll");
        System.out.println(map1);
    }


    @Test
    public void test4(){

        String APP_ID = "2019082666435508";
        String APP_PRIVATE_KEY = "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQC8md5bd1lowiY1fs2YLhrFWxliUp2dhmjnIozJIRAjIq6X3SFBbyKvD2pgFQeZqsCyb8H4OuvSZFjImZJu7EPWb/mDyrm1JUyQwt70AWSlL9YAA3aeffNWrCM4M7EfOmHSNkJkE4SF2uU92eXXyieBfcP8SAS/EvUIK938i7PGgDAHo6xUa1kB9GnR1LFtEWM6IxW40SYM1pWXRxyjR6Zy9xAieItFE10nUm54nRNq6kR0O7vd/wO7oiJewbOwDGdUJy+y/BtAk6W5IudcZHh0xx7xiX1btLvpgCswEcEgNaMHQkIfgPhZ25IgP6oI+HeLtKfrq5EiNFN4Y0dFRTpFAgMBAAECggEAXYQuLUxQmRErwPrPFtZFj/ZL7+TnImSzP2hyRoiOZ92alNKg2sEwgX0zUdEbo6Z3RgMEOwF8TSZUn6MGaNneCUYcgqzSyG9mN6Foy7SlIwNFKVyORKhPMHrKO+Plv4ZDWuhsoxLKc1TGtpSE0yr5y8uAkmdjcxsDRLn7qG3dBl9O2IjfKFhSq9J0MuotMa01Kx9Ud5Z9pCMYJGz7LcClHFsl9RkO6jMm1hAOEBsLmL47bLezyDFgs2f9UR8vApb5AXVBEDRPGiI8qVXbwyKApTOxb3xKg60/PGobMGxv8vD1W3qXPiK5IyO7iqzkQjiqpbiLG2DeEWlur7egwDsBQQKBgQDt7XVeRNBN1f8ManoADzyKAmA82SrB1PoBSkMr784KEtCKua4D8j7z939hZFZGmuLFlTM2c0jhfGZP08xrBZgbvgD5tWXi5BOSaVlHe04dC2Aq38KzO8AQ7ODnKm9gFo9x5e6XU8IJVyfwSJVic1kgsObtiKbRdQCUECIdpesDVwKBgQDK7T9i+Z1aC0Wn+xfjL/6kVruqbyFfF7bLcyrD/wJ5fAExdehetgMKM40wXvND5YF5vjyTB9m8HKDn5h3+6i/CwogAwYvjy+onIsTrsDHucnF9Lsj6x2j8VirddqNCKUeXggE2ImMdWXI5fRj057Po1DlddbSCRinKDJGz1VVpwwKBgQCFSLWsFm5+e/fBk9A+QkrOoSAaVimepdlbaaeRcGgCuqF6ZSP3b/gWITqJa3TtXozU1Iz43sIgFtkVtPdZqKuaNulC+XxXCx9vsHZkFc4fdSTtP6ZJU5p3oMsolFwJ/vlie8UZnsEfDGUCNRcCLM3sA02KGTgiNCIbz6Hc6/poRwKBgBmEZdEbVFcLTdIGuAVAdsPRiD2xRcAAamMA82K8AWKnXjsK+ZJXTuCMhPeNKQfjpzlY26MdfJqz+uPOQv1YE3NpJNL7g4y8ipArRhnhCRZT28ODC5DFXIVGjJNWpMbgC/mYRnC5OD9rjPp/qZC6RhxD+4auE2nCSuT2WagEbn4jAoGADlVyKfRoQgAIXb98s3sobsjQFsB3ySsQ+okSf7GFW2hCANRV/vxSUwNOuQ7fhuIcWq+IST3mWuwaAhOjpDv+oG/idQSblx2NPtgVNyse9xluVGV+gPwaiVanYfGnmS9xEEL7b0mfWQO6QeJkLQ69qU892Vk8BctYPyjetZ5o0e8=";
        String CHARSET = "UTF-8";
        String ALIPAY_PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAntWGCd9E7dayalOUP2+48AqMEODJRFRV17OKQQ7bO9yoTu6+iyXvp0dqd6FFJycLTc9VTRoOon3Kp7oLnKe/Umb+zrX5o/xv06uQqoNfiUAuFPuRv6kK8w188q3koHoIB549r2G8a74wJuvBaczw4OOsarEuGgWPtICULpw3+u5XvYKIm/htQgLLA7aHpcpboGD1l+2aRo/ZFVt673INEuB/G124BDdZzG6wo2RRecjY0uXhBbF9WVCqkW/lqS9KRPoCt8lJ95MR1BhcWORuUHJNRBxMC+2yHPd+g4pVgIjUz5X1Fx8JKSSI428F9bQxsXPNybiwnFmhQ7DPBs9pMwIDAQAB";

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                APP_ID, APP_PRIVATE_KEY, "json", CHARSET, ALIPAY_PUBLIC_KEY, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody("sa");
        model.setSubject("sa");
        model.setOutTradeNo("asdasdsdasdsad");
        model.setTimeoutExpress("30m");
        model.setTotalAmount("12");
        model.setPassbackParams("callback params");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl("https://www.shixianly.cn/system/alipay/notify_url");
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
    }


}
