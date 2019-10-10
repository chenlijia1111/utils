package com.github.chenlijia1111.utils.pay.ali;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.request.AlipayTradeAppPayRequest;
import com.alipay.api.request.AlipayTradeRefundRequest;
import com.alipay.api.request.AlipayTradeWapPayRequest;
import com.alipay.api.response.AlipayTradeAppPayResponse;
import com.alipay.api.response.AlipayTradeRefundResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chenlijia1111.utils.core.enums.CharSetType;
import com.github.chenlijia1111.utils.image.QRCodeUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 支付宝支付工具
 *
 * @author 陈礼佳
 * @since 2019/9/15 17:25
 */
public class ALiPayUtil {


    /**
     * app 支付 返回orderString 给app
     *
     * @param appId       appId
     * @param privateKey  私钥
     * @param publicKey   公钥
     * @param body        描述
     * @param orderNo     订单单号
     * @param totalAmount 支付金额 单位为元，精确到小数点后两位，取值范围[0.01,100000000]
     * @param notifyUrl   回调地址
     * @return
     */
    public static String appPay(String appId, String privateKey, String publicKey, String body,
                                String orderNo, String totalAmount, String notifyUrl) {

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                appId, privateKey, "json", CharSetType.UTF8.getType(), publicKey, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();
        //SDK已经封装掉了公共参数，这里只需要传入业务参数。以下方法为sdk的model入参方式(model和biz_content同时存在的情况下取biz_content)。
        AlipayTradeAppPayModel model = new AlipayTradeAppPayModel();
        model.setBody(body);
        model.setSubject(body);
        model.setOutTradeNo(orderNo);
        model.setTimeoutExpress("30m");
        model.setTotalAmount(totalAmount);
        model.setPassbackParams("callback params");
        model.setProductCode("QUICK_MSECURITY_PAY");
        request.setBizModel(model);
        request.setNotifyUrl(notifyUrl);
        try {
            //这里和普通的接口调用不同，使用的是sdkExecute
            AlipayTradeAppPayResponse response = alipayClient.sdkExecute(request);
            //System.out.println(response.getBody());//就是orderString 可以直接给客户端请求，无需再做处理。
            return response.getBody();
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 手机网页支付 直接返回支付页面 用response返回
     *
     * @param appId
     * @param privateKey
     * @param publicKey   公钥
     * @param orderNo     订单编号
     * @param orderAmount 订单金额
     * @param body        描述
     * @param returnUrl   支付成功之后的跳转地址
     * @param notifyUrl   回调地址
     * @return
     */
    public static void WAPPay(String appId, String privateKey, String publicKey, String orderNo, String orderAmount, String body,
                              String returnUrl, String notifyUrl, HttpServletResponse response) {

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                appId, privateKey, "json", CharSetType.UTF8.getType(), publicKey, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

        AlipayTradeWapPayModel model = new AlipayTradeWapPayModel();
        model.setBody(body);
        model.setSubject(body);
        model.setTotalAmount(orderAmount);
        model.setPassbackParams("1");
        model.setOutTradeNo(orderNo);
        model.setProductCode("QUICK_WAP_PAY");
        try {
            AlipayTradeWapPayRequest aliPayRequest = new AlipayTradeWapPayRequest();
            aliPayRequest.setReturnUrl(returnUrl);
            aliPayRequest.setNotifyUrl(notifyUrl);
            aliPayRequest.setBizModel(model);

            AlipayTradeAppPayResponse alipayTradeAppPayResponse = alipayClient.pageExecute(request);
            String form = alipayTradeAppPayResponse.getBody();
            response.setContentType("text/html;charset=" + CharSetType.UTF8.getType());
            PrintWriter out = response.getWriter();
            out.write(form);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * PC支付
     *
     * @param appId
     * @param privateKey
     * @param publicKey   公钥
     * @param orderNo     订单id
     * @param orderAmount 订单金额
     * @param body        描述
     * @param returnUrl   支付成功跳转地址
     * @param notifyUrl   回调地址
     * @param response
     */
    public static void PCPay(String appId, String privateKey, String publicKey, String orderNo, String orderAmount, String body,
                             String returnUrl, String notifyUrl, HttpServletResponse response) {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                appId, privateKey, "json", CharSetType.UTF8.getType(), publicKey, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

        AlipayTradePagePayModel model = new AlipayTradePagePayModel();

        model.setOutTradeNo(orderNo);
        model.setProductCode("FAST_INSTANT_TRADE_PAY");
        model.setTotalAmount(orderAmount);
        model.setSubject(body);
        model.setBody(body);
        model.setPassbackParams("passback_params");
        try {

            request.setBizModel(model);
            request.setNotifyUrl(notifyUrl);
            request.setReturnUrl(returnUrl);

            AlipayTradeAppPayResponse alipayTradeAppPayResponse = alipayClient.pageExecute(request);
            String form = alipayTradeAppPayResponse.getBody();
            response.setContentType("text/html;charset=" + CharSetType.UTF8.getType());
            PrintWriter out = response.getWriter();
            out.write(form);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 二维码支付
     *
     * @param appId
     * @param privateKey
     * @param publicKey
     * @param orderNo     订单编号
     * @param orderAmount 订单金额
     * @param body        描述
     * @param notifyUrl   回调地址
     * @param response
     */
    public static void QRCodePay(String appId, String privateKey, String publicKey, String orderNo, String orderAmount, String body,
                                 String notifyUrl, HttpServletResponse response) {

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                appId, privateKey, "json", CharSetType.UTF8.getType(), publicKey, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();
        model.setSubject(body);
        model.setTotalAmount(orderAmount);
        model.setStoreId(appId);
        model.setTimeoutExpress("5m");
        model.setOutTradeNo(orderNo);
        try {
            request.setBizModel(model);
            request.setNotifyUrl(notifyUrl);

            AlipayTradeAppPayResponse execute = alipayClient.execute(request);
            String resultStr = execute.getBody();
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap hashMap = objectMapper.readValue(resultStr, HashMap.class);
            Map json = (Map) hashMap.get("alipay_trade_precreate_response");
            if (json.get("code").toString().equals("10000")) {
                Object qr_code = json.get("qr_code");
                response.setHeader("Cache-Control", "no-store, no-cache");
                response.setContentType("image/png");
                new QRCodeUtil().output(qr_code.toString(), response.getOutputStream());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 转账
     *
     * @param appId
     * @param privateKey
     * @param publicKey
     * @param accountName 支付宝账号
     * @param userName    真实姓名
     * @param orderNo     订单编号
     * @param orderAmount 转账金额
     * @param body        转账描述
     */
    public static boolean transfer(String appId, String privateKey, String publicKey, String accountName, String userName,
                                   String orderNo, String orderAmount, String body) {

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                appId, privateKey, "json", CharSetType.UTF8.getType(), publicKey, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeAppPayRequest request = new AlipayTradeAppPayRequest();

        AlipayFundTransToaccountTransferModel model = new AlipayFundTransToaccountTransferModel();
        model.setOutBizNo(orderNo);
        model.setPayeeType("ALIPAY_LOGONID");
        model.setPayeeAccount(accountName);
        model.setAmount(orderAmount);
        model.setPayerShowName(userName);
        model.setPayerRealName(userName);
        model.setRemark(body);
        try {
            request.setBizModel(model);
            AlipayTradeAppPayResponse execute = alipayClient.execute(request);
            return execute.isSuccess();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 退款
     *
     * @param appId
     * @param privateKey
     * @param publicKey
     * @param orderNo       订单编号 与支付宝交易流水号 二选一
     * @param transactionNo 支付宝交易流水号 与订单编号二选一
     * @param orderAmount   订单金额 支付宝的退款单位为元 这里要注意一下
     * @param body          退款原因
     */
    public static AlipayTradeRefundResponse refund(String appId, String privateKey, String publicKey, String orderNo, String transactionNo,
                                                   String orderAmount, String body) {

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                appId, privateKey, "json", CharSetType.UTF8.getType(), publicKey, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradeRefundRequest request = new AlipayTradeRefundRequest();

        AlipayTradeRefundModel model = new AlipayTradeRefundModel();
        model.setOutTradeNo(orderNo);
        model.setTradeNo(transactionNo);
        model.setRefundAmount(orderAmount);
        model.setRefundReason(body);
        try {
            request.setBizModel(model);

            AlipayTradeRefundResponse execute = alipayClient.execute(request);
            return execute;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 处理回调
     * <p>
     * boolean verifyResult = AlipaySignature.rsaCheckV1(params, publicKey,
     * "UTF-8", "RSA2");
     * if (verifyResult) {
     * // TODO 商户的业务逻辑程序代码
     * //支付成功 获取流水号信息
     * String outTradeNo = params.get("out_trade_no");
     * String transactionId = params.get("trade_no");
     * //处理自己的业务逻辑
     * <p>
     * }
     *
     * @param request
     * @return
     */
    public static Map<String, String> notify(HttpServletRequest request) {

        Map<String, String> params = new HashMap();
        Map<String, String[]> requestParams = request.getParameterMap();
        Iterator iter = requestParams.keySet().iterator();

        while (iter.hasNext()) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";

            for (int i = 0; i < values.length; ++i) {
                valueStr = i == values.length - 1 ? valueStr + values[i] : valueStr + values[i] + ",";
            }

            params.put(name, valueStr);
        }

        return params;
    }

}
