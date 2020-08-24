package com.github.chenlijia1111.utils.pay.wx;

import com.github.chenlijia1111.utils.core.RandomUtil;
import com.github.chenlijia1111.utils.encrypt.RSAUtils;
import com.github.chenlijia1111.utils.encrypt.SHA256EncryptUtil;
import com.github.chenlijia1111.utils.encrypt.enums.SignType;
import com.github.chenlijia1111.utils.http.HttpClientUtils;
import org.apache.http.entity.ContentType;

import java.util.Map;
import java.util.Objects;

/**
 * 微信支付V3版本通用接口
 *
 * @author Chen LiJia
 * @since 2020/8/17
 */
public class WXPayV3CommonApiUtil {


    private static volatile WXPayV3CommonApiUtil instance;

    private WXPayV3CommonApiUtil() {
    }

    /**
     * 单例
     * @return
     */
    public static WXPayV3CommonApiUtil getInstance(){
        if(Objects.isNull(instance)){
            synchronized (WXPayV3CommonApiUtil.class){
                if(Objects.isNull(instance)){
                    instance = new WXPayV3CommonApiUtil();
                }
            }
        }
        return instance;
    }


    /**
     * 图片上传 api
     * POST https://api.mch.weixin.qq.com/v3/merchant/media/upload
     *
     * @param mchId      商户id
     * @param serialNo   证书序列号 登陆商户平台【API安全】->【API证书】->【查看证书】，可查看商户API证书序列号。
     * @param privateKey 商户私钥-即 apiclient_key.pem 里 BEGIN PRIVATE KEY-----和-----END PRIVATE KEY-----之间的内容
     * @param fileName   文件名称
     * @param fileBytes  文件字节数组内容
     * @return
     */
    public Map uploadImage(String mchId, String serialNo, String privateKey, String fileName, byte[] fileBytes) {

        //请求内容 {"filename":" filea.jpg ","sha256":" hjkahkjsjkfsjk78687dhjahdajhk "}
        //计算 sha值
        String sha256BytesToHexString = SHA256EncryptUtil.SHA256BytesToHexString(fileBytes);
        String meta = String.format("{\"filename\":\"%s\",\"sha256\":\"%s\"}", fileName, sha256BytesToHexString);

        //拼装http头的Authorization内容
        String authorization = authorization(mchId, serialNo, privateKey,
                "POST", "/v3/merchant/media/upload", meta);

        //开始请求
        Map map = HttpClientUtils.getInstance().
                putHeader("Authorization", authorization).
                putFileByteParams("file", fileBytes, fileName, ContentType.IMAGE_JPEG).
                putParams("meta", meta).
                putFileParamsContentType("meta", ContentType.APPLICATION_JSON).
                doPost("https://api.mch.weixin.qq.com/v3/merchant/media/upload").
                toMap();

        return map;
    }

    /**
     * 生成 authorization 头部信息
     *
     * @param mchId       商户号
     * @param serialNo    证书序列号 登陆商户平台【API安全】->【API证书】->【查看证书】，可查看商户API证书序列号。
     * @param privateKey  商户私钥-即 apiclient_key.pem 里 BEGIN PRIVATE KEY-----和-----END PRIVATE KEY-----之间的内容
     * @param methodType  请求类型 POST GET
     * @param requestPath 请求地址 域名后面的那一串 如：/v3/merchant/media/upload
     * @param requestBody
     * @return
     */
    public String authorization(String mchId, String serialNo, String privateKey,
                                       String methodType, String requestPath, String requestBody) {
        //构建签名字符串
        StringBuilder signStr = new StringBuilder();
        signStr.append(methodType);
        signStr.append("\n");
        signStr.append(requestPath);
        signStr.append("\n");
        //时间戳
        long timeStamp = System.currentTimeMillis() / 1000;
        signStr.append(timeStamp);
        signStr.append("\n");
        //随机字符串
        String nonceStr = RandomUtil.createUUID();
        signStr.append(nonceStr);
        signStr.append("\n");
        signStr.append(requestBody);
        signStr.append("\n");
        signStr.append("\n");

        //进行签名 SHA256 with RSA
        String sign = RSAUtils.sign(privateKey, signStr.toString(), SignType.SHA256_WITH_RSA);

        //拼装http头的Authorization内容
        String authorization = "WECHATPAY2-SHA256-RSA2048 mchid=\"" + mchId + "\",nonce_str=\"" + nonceStr + "\",signature=\"" + sign + "\",timestamp=\"" + timeStamp + "\",serial_no=\"" + serialNo + "\"";
        return authorization;
    }

    /**
     * 获取平台证书列表
     * 用于校验返回值 进行验签
     *
     * @return
     */
    public Map certificates(){
        return null;
    }

}
