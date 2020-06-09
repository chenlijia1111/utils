package com.github.chenlijia1111.utils.pay.ali;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.AlipayResponse;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.domain.*;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.*;
import com.alipay.api.response.*;
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
 * 注意：这里的公钥是支付宝公钥，不是应用公钥，不要弄错了。
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
        AlipayTradePagePayRequest request = new AlipayTradePagePayRequest();

        AlipayTradePagePayModel model = new AlipayTradePagePayModel();
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

            AlipayTradePagePayResponse alipayTradePagePayResponse = alipayClient.pageExecute(request);
            String form = alipayTradePagePayResponse.getBody();
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
     * 返回二维码地址
     *
     * 正确返回数据
     * {@code
     *  {
     *     "alipay_trade_precreate_response": {
     *         "code": "10000",
     *         "msg": "Success",
     *         "out_trade_no": "6823789339978248",
     *         "qr_code": "https://qr.alipay.com/bavh4wjlxf12tper3a"
     *     },
     *     "sign": "ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE"
     *  }
     * }
     *
     * 错误返回数据
     * {@code
     *  {
     *     "alipay_trade_precreate_response": {
     *         "code": "20000",
     *         "msg": "Service Currently Unavailable",
     *         "sub_code": "isp.unknow-error",
     *         "sub_msg": "系统繁忙"
     *     },
     *     "sign": "ERITJKEIJKJHKKKKKKKHJEREEEEEEEEEEE"
     * }
     * }
     *
     * @param appId
     * @param privateKey
     * @param publicKey   公钥
     * @param orderNo     订单id
     * @param orderAmount 订单金额
     * @param body        描述
     * @param returnUrl   支付成功跳转地址
     * @param notifyUrl   回调地址
     */
    public static AlipayTradePrecreateResponse PCPay(String appId, String privateKey, String publicKey, String orderNo, String orderAmount, String body,
                             String returnUrl, String notifyUrl) {
        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                appId, privateKey, "json", CharSetType.UTF8.getType(), publicKey, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayTradePrecreateRequest request = new AlipayTradePrecreateRequest();

        AlipayTradePrecreateModel model = new AlipayTradePrecreateModel();

        model.setOutTradeNo(orderNo);
        model.setTotalAmount(orderAmount);
        model.setSubject(body);
        model.setBody(body);
        try {

            request.setBizModel(model);
            request.setNotifyUrl(notifyUrl);
            request.setReturnUrl(returnUrl);

            AlipayTradePrecreateResponse alipayTradePrecreateResponse = alipayClient.execute(request);
            return alipayTradePrecreateResponse;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
    public static AlipayResponse transfer(String appId, String privateKey, String publicKey, String accountName, String userName,
                                          String orderNo, String orderAmount, String body) {

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                appId, privateKey, "json", CharSetType.UTF8.getType(), publicKey, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayFundTransToaccountTransferRequest request = new AlipayFundTransToaccountTransferRequest();

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
            AlipayFundTransToaccountTransferResponse response = alipayClient.execute(request);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 转账到银行卡
     *
     * @param appId
     * @param privateKey
     * @param publicKey
     * @param userName    真实姓名
     * @param orderNo     订单编号
     * @param orderAmount 转账金额
     * @param body        转账描述
     */
    public static AlipayResponse transferToBank(String appId, String privateKey, String publicKey, String bankCardNo, String userName,
                                                String orderNo, String orderAmount, String body) {

        //实例化客户端
        AlipayClient alipayClient = new DefaultAlipayClient("https://openapi.alipay.com/gateway.do",
                appId, privateKey, "json", CharSetType.UTF8.getType(), publicKey, "RSA2");
        //实例化具体API对应的request类,类名称和接口名称对应,当前调用接口名称：alipay.trade.app.pay
        AlipayFundTransTobankTransferRequest request = new AlipayFundTransTobankTransferRequest();


        AlipayFundTransTobankTransferModel model = new AlipayFundTransTobankTransferModel();
        model.setAmount(orderAmount); //转账金额，单位：元。支持2位小数，小数点前最大支持13位，金额必须大于0。
        model.setMemo("1");
        model.setOutBizNo(orderNo);
        model.setPayeeAccountName(userName); //收款方银行账户名，必须与收款方银行卡号所属账户信息一致。
        model.setPayeeCardNo(bankCardNo);
        model.setRemark(body);
        try {
            request.setBizModel(model);
            AlipayFundTransTobankTransferResponse response = alipayClient.execute(request);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
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
     * 验证签名是否正确
     * boolean verifyResult = AlipaySignature.rsaCheckV1(params, publicKey,
     * "UTF-8", "RSA2");
     * if (verifyResult) {
     *
     * //支付成功 获取流水号信息
     * String outTradeNo = params.get("out_trade_no");
     * String transactionId = params.get("trade_no");
     * //处理自己的业务逻辑
     * <p>
     * }
     *
     * 需要返回支付宝 success/failure
     *
     * 这里只是解析了返回的参数
     * 调用者需要自己校验数据的合法性，防止恶意请求
     * {@link AlipaySignature#rsaCheckV1(Map, String, String, String)}
     * 需要传入signType参数，因为在请求的时候传了signType参数为RSA2
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
