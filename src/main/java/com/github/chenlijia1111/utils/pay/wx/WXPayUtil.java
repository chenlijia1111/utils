package com.github.chenlijia1111.utils.pay.wx;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.common.constant.ContentTypeConstant;
import com.github.chenlijia1111.utils.core.RandomUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.enums.CharSetType;
import com.github.chenlijia1111.utils.encrypt.MD5EncryptUtil;
import com.github.chenlijia1111.utils.encrypt.RSAUtils;
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
 * <p>
 * 修改-2020-06-23
 * 微信回调 校验签名好像不对，通过微信官方的签名测试生成的签名也是对不上的，不知道微信是用的哪些参数进行的签名
 * 但是可以在接收到回调之后查询订单的详情，然后进行校验，查询详情的签名是可以校验成功的
 * {@link #checkNotify(Map, String)}
 *
 * @author 陈礼佳
 * @since 2019/9/15 12:40
 */
public class WXPayUtil {


    /**
     * 下预订单
     *
     * @param appId
     * @param mchId      商户号
     * @param body       商品描述 128字符
     * @param totalFee   交易金额 以分为单位
     * @param signKey    签名加盐的key key设置路径：微信商户平台(pay.weixin.qq.com)-->账户设置-->API安全-->密钥设置
     * @param notifyUrl  回调地址
     * @param payType    支付客户端类型
     * @param openId     当支付类型为 {@link PayType#JSAPI} 需要传openId 此参数为微信用户在商户对应appid下的唯一标识
     * @param outTradeNo 商户订单号
     * @param request
     * @return
     */
    public static Map createPreOrder(String appId, String mchId, String body, String signKey, int totalFee,
                                     String notifyUrl, PayType payType, String openId, String outTradeNo, HttpServletRequest request) {

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
        if (Objects.equals(map.get("return_code").toString(), "SUCCESS") &&
                Objects.equals(map.get("result_code").toString(), "SUCCESS")) {
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
                TreeMap<String, Object> treeMap = new TreeMap<>();
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
     * <p>
     * {@code
     * {
     * transaction_id=4200000599202006232298777759,
     * nonce_str=0024fccce85e444dbf438879d5f181a0,
     * bank_type=OTHERS, openid=UesAIQgk7bcGHj_5A,
     * sign=E9626A483109EA27FE282B5E62668968,
     * fee_type=CNY,
     * mch_id=1600453107,
     * cash_fee=300,
     * out_trade_no=472820029759164416,
     * appid=wxaa43502dfae4e166,
     * total_fee=300,
     * trade_type=APP,
     * result_code=SUCCESS,
     * time_end=20200623145911,
     * is_subscribe=N,
     * return_code=SUCCESS
     * }
     * }
     * <p>
     * <p>
     * 注意，调用者需要返回微信表明已取到数据
     * 微信希望最终返回数据格式
     * <xml>
     * <return_code><![CDATA[SUCCESS]]></return_code>
     * <return_msg><![CDATA[OK]]></return_msg>
     * </xml>
     * <p>
     * 这里只是获取回调的参数，具体的校验需要另外做，防止恶意请求
     * 可以通过 {@link #createSign(Map, String)} 进行校验
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
     * 校验支付回调信息是否正确
     *
     * @param notifyParams 回调参数
     * @param partnerKey   加密密钥
     * @return
     */
    public static boolean checkNotify(Map notifyParams, String partnerKey) {

        boolean checkResult = false;

        if (Objects.nonNull(notifyParams) &&
                StringUtils.isNotEmpty(partnerKey) &&
                notifyParams.containsKey("appid") &&
                notifyParams.containsKey("mch_id") &&
                Objects.equals("SUCCESS", notifyParams.get("return_code")) &&
                Objects.equals("SUCCESS", notifyParams.get("result_code")) &&
                notifyParams.containsKey("out_trade_no") &&
                notifyParams.containsKey("transaction_id")) {

            String appid = notifyParams.get("appid").toString();
            String mch_id = notifyParams.get("mch_id").toString();
            //商户流水号
            String outTradeNo = notifyParams.get("out_trade_no").toString();
            //支付流水号
            String transactionId = notifyParams.get("transaction_id").toString();


            //查询支付订单详情
            Map orderInoMap = queryOrderInfo(appid, mch_id, partnerKey, transactionId, outTradeNo);

            if (Objects.nonNull(orderInoMap) &&
                    orderInoMap.containsKey("sign") &&
                    Objects.equals("SUCCESS", orderInoMap.get("return_code")) &&
                    Objects.equals("SUCCESS", orderInoMap.get("result_code")) &&
                    Objects.equals("SUCCESS", orderInoMap.get("trade_state"))) {

                //校验详情的签名是否正确
                String orderInfoSign = orderInoMap.get("sign").toString();
                //构建签名
                String localSign = createSign(orderInoMap, partnerKey);

                checkResult = Objects.equals(orderInfoSign, localSign);
            }
        }

        return checkResult;
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
     * 转账银行卡
     * 转账银行卡需要先去获取RSA公钥对敏感字段先进行加密,然后再用加密密钥进行签名
     * 获取公钥文档 https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_7&index=4
     * 银行编号文档 https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_4
     *
     * @param mchid              商户号
     * @param nonceStr           随机字符串
     * @param partnerTradeNo     转账单号
     * @param encBankNo          银行卡号
     * @param encTrueName        真是姓名
     * @param bankCode           银行编号
     * @param amount             金额 分
     * @param desc               描述
     * @param sslFileInputStream SSL加密文件输入流
     * @param signKey            签名加盐秘钥
     * @param rsaPublicKey       参数rsa加密公钥
     * @return java.util.Map
     * @since 下午 9:53 2019/9/24 0024
     **/
    public static Map transferToBank(String mchid, String nonceStr, String partnerTradeNo, String encBankNo,
                                     String encTrueName, String bankCode, String amount, String desc, InputStream sslFileInputStream, String signKey, String rsaPublicKey) {

        HttpClientUtils instance = HttpClientUtils.getInstanceWithSSL(sslFileInputStream, mchid);

        instance.putParams("mchid", mchid); //商户号
        instance.putParams("partner_trade_no", partnerTradeNo); //商户企业付款单号
        instance.putParams("nonce_str", nonceStr); //随机字符串

        //银行卡号进行RSA加密
        encBankNo = RSAUtils.publicEncrypt(encBankNo, rsaPublicKey);
        instance.putParams("enc_bank_no", encBankNo); //收款方银行卡号
        //收款方用户名进行RSA加密
        encTrueName = RSAUtils.publicEncrypt(encTrueName, rsaPublicKey);
        instance.putParams("enc_true_name", encTrueName); //收款方用户名
        instance.putParams("bank_code", bankCode); //收款方开户行
        instance.putParams("amount", amount); //付款金额
        instance.putParams("desc", desc); //付款说明

        String s = instance.paramsToString(true);

        s = s + "&key=" + signKey;
        //构建签名
        String sign = MD5EncryptUtil.MD5StringToHexString(s);
        instance.putParams("sign", sign);

        String xmlString = instance.setContentType(ContentTypeConstant.TEXT_XML).doPost("https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank").toString();
        Map<String, Object> map = XmlUtil.parseXMLToMap(xmlString);
        return map;
    }

    /**
     * 转账银行卡
     * 转账银行卡需要先去获取RSA公钥对敏感字段先进行加密,然后再用加密密钥进行签名
     * 获取公钥文档 https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_7&index=4
     * 银行编号文档 https://pay.weixin.qq.com/wiki/doc/api/tools/mch_pay.php?chapter=24_4
     *
     * @param mchid          商户号
     * @param nonceStr       随机字符串
     * @param partnerTradeNo 转账单号
     * @param encBankNo      银行卡号
     * @param encTrueName    真是姓名
     * @param bankCode       银行编号
     * @param amount         金额 分
     * @param desc           描述
     * @param sslFilePath    SSL加密文件
     * @param signKey        签名加盐秘钥
     * @param rsaPublicKey   参数rsa加密公钥
     * @return java.util.Map
     * @since 下午 9:53 2019/9/24 0024
     **/
    public static Map transferToBank(String mchid, String nonceStr, String partnerTradeNo, String encBankNo,
                                     String encTrueName, String bankCode, String amount, String desc, String sslFilePath, String signKey, String rsaPublicKey) {

        File file = new File(sslFilePath);
        HttpClientUtils instance = HttpClientUtils.getInstanceWithSSL(file, mchid);

        instance.putParams("mchid", mchid); //商户号
        instance.putParams("partner_trade_no", partnerTradeNo); //商户企业付款单号
        instance.putParams("nonce_str", nonceStr); //随机字符串

        //银行卡号进行RSA加密
        encBankNo = RSAUtils.publicEncrypt(encBankNo, rsaPublicKey);
        instance.putParams("enc_bank_no", encBankNo); //收款方银行卡号
        //收款方用户名进行RSA加密
        encTrueName = RSAUtils.publicEncrypt(encTrueName, rsaPublicKey);
        instance.putParams("enc_true_name", encTrueName); //收款方用户名
        instance.putParams("bank_code", bankCode); //收款方开户行
        instance.putParams("amount", amount); //付款金额
        instance.putParams("desc", desc); //付款说明

        String s = instance.paramsToString(true);

        s = s + "&key=" + signKey;
        //构建签名
        String sign = MD5EncryptUtil.MD5StringToHexString(s);
        instance.putParams("sign", sign);

        String xmlString = instance.setContentType(ContentTypeConstant.TEXT_XML).doPost("https://api.mch.weixin.qq.com/mmpaysptrans/pay_bank").toString();
        Map<String, Object> map = XmlUtil.parseXMLToMap(xmlString);
        return map;
    }


    /**
     * 转账银行卡-获取参数加密rsa公钥
     *
     * @param mchId
     * @param sslFilePath ssl加密文件地址
     * @param signKey     签名密钥
     * @return
     */
    public static Map fieldRSAPublicKey(String mchId, String sslFilePath, String signKey) {
        File file = new File(sslFilePath);
        HttpClientUtils instance = HttpClientUtils.getInstanceWithSSL(file, mchId);
        instance.putParams("mchid", mchId); //商户号
        String nonceStr = RandomUtil.createUUID();
        instance.putParams("nonce_str", nonceStr); //随机字符串
        instance.putParams("sign_type", "MD5"); //签名类型

        String s = instance.paramsToString(true);

        s = s + "&key=" + signKey;
        //构建签名
        String sign = MD5EncryptUtil.MD5StringToHexString(s);
        instance.putParams("sign", sign);

        String xmlString = instance.setContentType(ContentTypeConstant.TEXT_XML).doPost("https://fraud.mch.weixin.qq.com/risk/getpublickey").toString();
        Map<String, Object> map = XmlUtil.parseXMLToMap(xmlString);
        return map;
    }

    /**
     * 转账银行卡-获取参数加密rsa公钥
     *
     * @param mchId
     * @param sslFileInputStream ssl加密文件输入流
     * @param signKey            签名密钥
     * @return
     */
    public static Map fieldRSAPublicKey(String mchId, InputStream sslFileInputStream, String signKey) {
        HttpClientUtils instance = HttpClientUtils.getInstanceWithSSL(sslFileInputStream, mchId);
        instance.putParams("mchid", mchId); //商户号
        String nonceStr = RandomUtil.createUUID();
        instance.putParams("nonce_str", nonceStr); //随机字符串
        instance.putParams("sign_type", "MD5"); //签名类型

        String s = instance.paramsToString(true);

        s = s + "&key=" + signKey;
        //构建签名
        String sign = MD5EncryptUtil.MD5StringToHexString(s);
        instance.putParams("sign", sign);

        String xmlString = instance.setContentType(ContentTypeConstant.TEXT_XML).doPost("https://fraud.mch.weixin.qq.com/risk/getpublickey").toString();
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
    public static String createSign(Map params, String partnerKey) {
        // 生成签名前先去除sign
        params.remove("sign");
        String tempStr = HttpClientUtils.getInstance().putParams(params).paramsToString(true);
        String stringSignTemp = tempStr + "&key=" + partnerKey;
        String sign = MD5EncryptUtil.MD5StringToHexString(stringSignTemp);
        return sign;
    }

    /**
     * 查询订单详情
     * <p>
     * {@code
     * {
     * "transaction_id":"4200000601202006233274481066",
     * "nonce_str":"i9lORh3f8khZpIgK",
     * "trade_state":"REFUND",
     * "bank_type":"OTHERS",
     * "openid":"o0LL8wgeaJ-UesAIQgk7bcGHj_5A",
     * "sign":"C071DF8B3AB0B613DB0C8F370ACDD54B",
     * "return_msg":"OK",
     * "fee_type":"CNY",
     * "mch_id":"1600453107",
     * "cash_fee":"300",
     * "out_trade_no":"472834531779153920",
     * "cash_fee_type":"CNY",
     * "appid":"wxaa43502dfae4e166",
     * "total_fee":"300",
     * "trade_state_desc":"订单发生过退款，退款详情请查询退款单",
     * "trade_type":"APP",
     * "result_code":"SUCCESS",
     * "attach":null,
     * "time_end":"20200623155648",
     * "is_subscribe":"N",
     * "return_code":"SUCCESS"
     * }
     * }
     * <p>
     * trade_state：SUCCESS—支付成功
     * REFUND—转入退款
     * NOTPAY—未支付
     * CLOSED—已关闭
     * REVOKED—已撤销（刷卡支付）
     * USERPAYING--用户支付中
     * PAYERROR--支付失败(其他原因，如银行返回失败)
     * 判断支付是否成功可通过 return_code:SUCCESS  result_code:SUCCESS  trade_type:SUCCESS
     *
     * @param appId
     * @param mchId
     * @param partnerKey    密钥
     * @param transactionId 微信流水
     * @param outTradeNo    商家流水
     * @return
     */
    public static Map queryOrderInfo(String appId, String mchId, String partnerKey, String transactionId, String outTradeNo) {
        //校验参数
        AssertUtil.hasText(appId, "appId不能为空");
        AssertUtil.hasText(mchId, "mchId不能为空");
        AssertUtil.hasText(partnerKey, "partnerKey不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(transactionId) || StringUtils.isNotEmpty(outTradeNo), "流水号二选一");

        HttpClientUtils httpClientUtils = HttpClientUtils.getInstance();
        httpClientUtils.putParams("appid", appId).
                putParams("mch_id", mchId).
                putParams("nonce_str", RandomUtil.createUUID()).
                putParams("out_trade_no", "472834531779153920");
        if (StringUtils.isNotEmpty(transactionId)) {
            httpClientUtils.putParams("transaction_id", transactionId);
        }
        if (StringUtils.isNotEmpty(outTradeNo)) {
            httpClientUtils.putParams("out_trade_no", outTradeNo);
        }

        String paramsString = httpClientUtils.paramsToString(true);
        //签名
        String sign = MD5EncryptUtil.MD5StringToHexString(paramsString + "&key=" + partnerKey);
        httpClientUtils.putParams("sign", sign);
        String s = httpClientUtils.setContentType("text/xml; charset=UTF-8").doPost("https://api.mch.weixin.qq.com/pay/orderquery").toString();
        Map<String, Object> map = XmlUtil.parseXMLToMap(s);
        return map;
    }


}
