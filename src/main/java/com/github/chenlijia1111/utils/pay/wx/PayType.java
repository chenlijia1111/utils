package com.github.chenlijia1111.utils.pay.wx;

/**
 * 微信支付 支付类别
 *
 * @author 陈礼佳
 * @since 2019/9/15 13:56
 */
public enum PayType {

    JSAPI("JSAPI"), //JSAPI支付(或小程序支付)  统一下单之后需要 将预订单号二次签名传给前端调起微信支付
    NATIVE("NATIVE"), //Native支付 统一下单之后 直接将结果返回 结果里会有一个二维码地址
    APP("APP"), //app支付 统一下单之后需要 将预订单号二次签名传给前端调起微信支付
    MWEB("MWEB"); //H5支付 通过统一下单之后的mweb_url参数进行跳转即可

    PayType(String type) {
        this.type = type;
    }

    private String type;


    public String getType() {
        return type;
    }
}
