package com.github.chenlijia1111.utils.pay.wx;

import com.github.chenlijia1111.utils.common.constant.ContentTypeConstant;
import com.github.chenlijia1111.utils.core.RandomUtil;
import com.github.chenlijia1111.utils.core.enums.CharSetType;
import com.github.chenlijia1111.utils.encrypt.MD5EncryptUtil;
import com.github.chenlijia1111.utils.http.HttpClientUtils;
import com.github.chenlijia1111.utils.http.HttpUtils;
import com.github.chenlijia1111.utils.http.URLBuildUtil;
import com.github.chenlijia1111.utils.xml.XmlUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * 微信支付工具
 *
 * @author 陈礼佳
 * @since 2019/9/15 12:40
 */
public class WXPayUtil {


    /**
     * 下预订单
     *
     * @param appId
     * @param mchId     商户号
     * @param body      商品描述 128字符
     * @param totalFee  交易金额 以分为单位
     * @param signKey   签名加盐的key key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
     * @param notifyUrl 回调地址
     * @param payType   支付客户端类型
     * @param openId    当支付类型为 {@link PayType#JSAPI} 需要传openId
     * @param outTradeNo 商户订单号
     * @param request
     * @return
     */
    public static Map createPreOrder(String appId, String mchId, String body, String signKey, int totalFee,
                                     String notifyUrl, PayType payType, String openId,String outTradeNo, HttpServletRequest request) {

        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        //填充参数
        httpClientUtils.
                putParams("appid", appId). //微信支付分配的公众账号ID（企业号corpid即为此appId）
                putParams("mch_id", mchId). //微信支付分配的商户号
                putParams("nonce_str", RandomUtil.createUUID()). //随机字符串，长度要求在32位以内
                putParams("sign_type", "MD5"). //签名类型，默认为MD5，支持HMAC-SHA256和MD5
                putParams("body", body). //商品简单描述
                putParams("out_trade_no", outTradeNo). //商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
                putParams("total_fee", totalFee + ""). //订单总金额，单位为分
                putParams("spbill_create_ip", HttpUtils.getIpAddr(request)). //终端ip
                putParams("notify_url", notifyUrl). //回调地址
                putParams("trade_type", payType.getType()); //交易类型

        if (Objects.equals(PayType.JSAPI, payType)) {
            httpClientUtils.putParams("openid", openId);
        }

        //进行参数的签名 MD5
        String paramsString = httpClientUtils.paramsToString(true);
        String sign = MD5EncryptUtil.MD5StringToHexString(paramsString + "&key=" + signKey);
        httpClientUtils.putParams("sign", sign);
        //发起请求
        String xmlString = httpClientUtils.setContentType(ContentTypeConstant.TEXT_XML).doPost("https://api.mch.weixin.qq.com/pay/unifiedorder").toString();
        Map<String, Object> map = XmlUtil.parseXMLToMap(xmlString);

        //获取到 prepay_id 预支付id 在通过签名 把预支付id返回给前端调起支付
        //如果是native 支付的话 直接就返回一个二维码的请求地址,不用进行二次签名
        if (Objects.equals(map.get("return_code"), "SUCCESS") &&
                Objects.equals(map.get("result_code"), "SUCCESS")) {
            //app 进行二次签名 调起微信支付
            if (Objects.equals(PayType.APP, payType)) {
                Object prepay_id = map.get("prepay_id");
                //二次签名
                TreeMap<String, String> treeMap = new TreeMap<>();
                treeMap.put("appid", appId);
                treeMap.put("partnerid", mchId);
                treeMap.put("prepayid", prepay_id.toString());
                treeMap.put("package", "Sign=WXPay");
                treeMap.put("noncestr", RandomUtil.createUUID());
                treeMap.put("timestamp", (System.currentTimeMillis() / 1000) + "");

                //创建请求参数
                String paramsToString = new URLBuildUtil("").putParams(treeMap).paramsToString();
                //进行签名
                String secondSign = MD5EncryptUtil.MD5StringToHexString(paramsToString + "&key=" + signKey);
                treeMap.put("sign", secondSign);
                return treeMap;
            }

            //小程序支付 进行二次签名 调起微信支付
            if (Objects.equals(PayType.JSAPI, payType)) {
                Object prepay_id = map.get("prepay_id");
                //二次签名
                TreeMap<String, String> treeMap = new TreeMap<>();
                treeMap.put("appId", appId);
                treeMap.put("package", "prepay_id=" + prepay_id.toString());
                treeMap.put("signType", "MD5");
                treeMap.put("nonceStr", RandomUtil.createUUID());
                treeMap.put("timeStamp", (System.currentTimeMillis() / 1000) + "");

                //创建请求参数
                String paramsToString = HttpClientUtils.getInstance().putParams(treeMap).paramsToString(true);
                //进行签名
                String secondSign = MD5EncryptUtil.MD5StringToHexString(paramsToString + "&key=" + signKey);
                treeMap.put("paySign", secondSign);
                return treeMap;
            }

        }

        //请求失败,或者是扫码的，直接返回map
        return map;
    }

    /**
     * 退款
     *
     * @param appId
     * @param mchId
     * @param signKey       签名密钥
     * @param sslFile       ssl加密文件
     * @param sslPassword   ssl密码 默认是商户号 即 mchId
     * @param transactionId 微信交易流水号 与商家内部订单号 二选一
     * @param outTradeNo    商家内部订单号
     * @param totalFee      订单总金额
     * @param refund_fee    退款金额
     * @return
     */
    public static Map refund(String appId, String mchId, String signKey, File sslFile,
                             String sslPassword, String transactionId,
                             String outTradeNo, int totalFee, int refund_fee) {

        HttpClientUtils httpClientUtils = HttpClientUtils.getInstanceWithSSL(sslFile, sslPassword);
        //填充参数
        httpClientUtils.
                putParams("appid", appId). //微信支付分配的公众账号ID（企业号corpid即为此appId）
                putParams("mch_id", mchId). //微信支付分配的商户号
                putParams("nonce_str", RandomUtil.createUUID()). //随机字符串，长度要求在32位以内
                putParams("sign_type", "MD5"). //签名类型，默认为MD5，支持HMAC-SHA256和MD5
                putParams("transaction_id", transactionId). //微信生成的订单号，在支付通知中有返回
                putParams("out_trade_no", outTradeNo). //商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
                putParams("out_refund_no", RandomUtil.createUUID()). //商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
                putParams("total_fee", totalFee + ""). //订单总金额，单位为分，只能为整数
                putParams("refund_fee", refund_fee + ""); //退款总金额，订单总金额，单位为分，只能为整数

        //构造签名
        //进行参数的签名 MD5
        String paramsString = httpClientUtils.paramsToString(true);
        String sign = MD5EncryptUtil.MD5StringToHexString(paramsString + "&key=" + signKey);
        httpClientUtils.putParams("sign", sign);

        //发送请求
        String s = httpClientUtils.setContentType(ContentTypeConstant.TEXT_XML).doPost("https://api.mch.weixin.qq.com/secapi/pay/refund").toString();
        Map<String, Object> map = XmlUtil.parseXMLToMap(s);
        return map;
    }

    /**
     * 退款
     *
     * @param appId
     * @param mchId
     * @param signKey            签名密钥
     * @param sslFileInputStream ssl加密文件输入流
     * @param sslPassword        ssl密码 默认是商户号 即 mchId
     * @param transactionId      微信交易流水号 与商家内部订单号 二选一
     * @param outTradeNo         商家内部订单号
     * @param totalFee           订单总金额
     * @param refund_fee         退款金额
     * @return
     */
    public static Map refund(String appId, String mchId, String signKey, InputStream sslFileInputStream,
                             String sslPassword, String transactionId,
                             String outTradeNo, int totalFee, int refund_fee) {

        HttpClientUtils httpClientUtils = HttpClientUtils.getInstanceWithSSL(sslFileInputStream, sslPassword);
        //填充参数
        httpClientUtils.
                putParams("appid", appId). //微信支付分配的公众账号ID（企业号corpid即为此appId）
                putParams("mch_id", mchId). //微信支付分配的商户号
                putParams("nonce_str", RandomUtil.createUUID()). //随机字符串，长度要求在32位以内
                putParams("sign_type", "MD5"). //签名类型，默认为MD5，支持HMAC-SHA256和MD5
                putParams("transaction_id", transactionId). //微信生成的订单号，在支付通知中有返回
                putParams("out_trade_no", outTradeNo). //商户系统内部订单号，要求32个字符内，只能是数字、大小写字母_-|* 且在同一个商户号下唯一
                putParams("out_refund_no", RandomUtil.createUUID()). //商户系统内部的退款单号，商户系统内部唯一，只能是数字、大小写字母_-|*@ ，同一退款单号多次请求只退一笔。
                putParams("total_fee", totalFee + ""). //订单总金额，单位为分，只能为整数
                putParams("refund_fee", refund_fee + ""); //退款总金额，订单总金额，单位为分，只能为整数

        //构造签名
        //进行参数的签名 MD5
        String paramsString = httpClientUtils.paramsToString(true);
        String sign = MD5EncryptUtil.MD5StringToHexString(paramsString + "&key=" + signKey);
        httpClientUtils.putParams("sign", sign);

        //发送请求
        String s = httpClientUtils.setContentType(ContentTypeConstant.TEXT_XML).doPost("https://api.mch.weixin.qq.com/secapi/pay/refund").toString();
        Map<String, Object> map = XmlUtil.parseXMLToMap(s);
        return map;
    }


    /**
     * 回调处理
     * 解析回调参数
     * 注意，调用者需要返回微信表明已取到数据
     * <xml>
     * <return_code><![CDATA[SUCCESS]]></return_code>
     * <return_msg><![CDATA[OK]]></return_msg>
     * </xml>
     *
     * @param request
     * @return
     */
    public static Map notify(HttpServletRequest request) {
        try {
            BufferedReader reader = request.getReader();
            StringBuilder sb = new StringBuilder();
            while (reader.ready()) {
                sb.append(reader.readLine());
            }

            Map<String, Object> map = XmlUtil.parseXMLToMap(new ByteArrayInputStream(sb.toString().getBytes(CharSetType.UTF8.getType())));
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 转账
     *
     * @param mchAppid
     * @param mchid
     * @param nonceStr       随机字符串
     * @param partnerTradeNo 转账单号
     * @param openid
     * @param amount         金额 分
     * @param desc           描述
     * @param spbillCreateIp 请求ip
     * @param sslFilePath    SSL加密文件
     * @param signKey        签名加盐秘钥
     * @return java.util.Map
     * @since 下午 9:53 2019/9/24 0024
     **/
    public static Map transfer(String mchAppid, String mchid, String nonceStr, String partnerTradeNo, String openid,
                               String amount, String desc, String spbillCreateIp, String sslFilePath, String signKey) {

        File file = new File(sslFilePath);
        HttpClientUtils instance = HttpClientUtils.getInstanceWithSSL(file, mchid);

        instance.putParams("mch_appid", mchAppid);
        instance.putParams("mchid", mchid);
        instance.putParams("nonce_str", nonceStr);
        instance.putParams("partner_trade_no", partnerTradeNo);
        instance.putParams("openid", openid);
        instance.putParams("check_name", "NO_CHECK");
        instance.putParams("amount", amount);
        instance.putParams("desc", desc);
        instance.putParams("spbill_create_ip", spbillCreateIp);

        String s = instance.paramsToString(true);

        s = s + "&key=" + signKey;
        //构建签名
        String sign = MD5EncryptUtil.MD5StringToHexString(s);
        instance.putParams("sign", sign);

        String xmlString = instance.setContentType(ContentTypeConstant.TEXT_XML).doPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers").toString();
        Map<String, Object> map = XmlUtil.parseXMLToMap(xmlString);
        return map;
    }


    /**
     * 转账
     *
     * @param mchAppid
     * @param mchid
     * @param nonceStr           随机字符串
     * @param partnerTradeNo     转账单号
     * @param openid
     * @param amount             金额 分
     * @param desc               描述
     * @param spbillCreateIp     请求ip
     * @param sslFileInputStream SSL加密文件输入流
     * @param signKey            签名加盐秘钥
     * @return java.util.Map
     * @since 下午 9:53 2019/9/24 0024
     **/
    public static Map transfer(String mchAppid, String mchid, String nonceStr, String partnerTradeNo, String openid,
                               String amount, String desc, String spbillCreateIp, InputStream sslFileInputStream, String signKey) {

        HttpClientUtils instance = HttpClientUtils.getInstanceWithSSL(sslFileInputStream, mchid);

        instance.putParams("mch_appid", mchAppid);
        instance.putParams("mchid", mchid);
        instance.putParams("nonce_str", nonceStr);
        instance.putParams("partner_trade_no", partnerTradeNo);
        instance.putParams("openid", openid);
        instance.putParams("check_name", "NO_CHECK");
        instance.putParams("amount", amount);
        instance.putParams("desc", desc);
        instance.putParams("spbill_create_ip", spbillCreateIp);

        String s = instance.paramsToString(true);

        s = s + "&key=" + signKey;
        //构建签名
        String sign = MD5EncryptUtil.MD5StringToHexString(s);
        instance.putParams("sign", sign);

        String xmlString = instance.setContentType(ContentTypeConstant.TEXT_XML).doPost("https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers").toString();
        Map<String, Object> map = XmlUtil.parseXMLToMap(xmlString);
        return map;
    }


    /**
     * 生成签名
     *
     * @param params     需要签名的参数
     * @param partnerKey 密钥
     * @return 签名后的数据
     */
    public static String createSign(Map<String, String> params, String partnerKey) {
        // 生成签名前先去除sign
        params.remove("sign");
        String tempStr = HttpClientUtils.getInstance().putParams(params).paramsToString(true);
        String stringSignTemp = tempStr + "&key=" + partnerKey;
        String sign = MD5EncryptUtil.MD5StringToHexString(stringSignTemp);
        return sign;
    }

}
