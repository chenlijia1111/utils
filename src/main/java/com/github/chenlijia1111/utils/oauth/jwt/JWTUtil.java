package com.github.chenlijia1111.utils.oauth.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.Objects;

/**
 * jwt 工具类
 * <p>
 * jwt 分为三部分 header payload sign
 * sign = HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)
 * base64UrlEncode 算法为 在经过了BASE64算法之后 把"="去掉，"+"用"-"替换，"/"用"_"替换
 *
 * @author 陈礼佳
 * @since 2019/9/13 21:28
 */
public class JWTUtil {


    /**
     * 创建一个 jwt 信息
     * 时间都是以秒为单位
     *
     * @param id        用户id
     * @param subject   用户信息
     * @param ttlMillis 有效时间
     * @return
     */
    public static String createJWT(String id, String subject, long ttlMillis, String signKey) {

        //签名类型 SHA-256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //创建用于签名的密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(signKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);


        //构建 JWT Claims
        JwtBuilder builder = Jwts.builder().setId(id)
                .setIssuedAt(now)
                .setSubject(subject)
                .signWith(signatureAlgorithm, signingKey);

        //设置过期时间
        if (ttlMillis >= 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        //构建
        return builder.compact();

    }


    /**
     * 解析 jwt
     * 在调用该方法前 应先调用
     *
     * @param jwt
     * @param signKey
     * @return
     * @see #checkFormat(String) 校验格式是否正确
     * @see #checkSign(String, String) 校验签名是否正确
     * @see #checkExpired(String, String) 校验是否过期
     */
    public static Claims parseJWT(String jwt, String signKey) {

        //开始解析
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(signKey))
                .parseClaimsJws(jwt).getBody();
        return claims;
    }


    /**
     * 校验 jwt 格式是否正确
     *
     * @param jwt
     * @return
     */
    public static boolean checkFormat(String jwt) {
        String[] split = jwt.split("\\.");
        return null != split && split.length == 3;
    }

    /**
     * 校验签名是否正确
     * 在调用该方法前 应先调用 {@link #checkFormat(String)} 校验格式
     *
     * @param jwt
     * @param signKey
     * @return
     */
    public static boolean checkSign(String jwt, String signKey) {

        String[] split = jwt.split("\\.");

        //签名类型 SHA-256
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        //创建用于签名的密钥
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(signKey);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
        DefaultJwtSignatureValidator validator = new DefaultJwtSignatureValidator(SignatureAlgorithm.HS256, signingKey);
        boolean valid = validator.isValid(split[0] + JwtParser.SEPARATOR_CHAR + split[1], split[2]);
        return valid;
    }

    /**
     * 校验jwt 是否过期
     * 在调用该方法前 应先调用 {@link #checkSign(String, String)}  校验签名
     *
     * @param jwt
     * @param signKey
     * @return
     */
    public static boolean checkExpired(String jwt, String signKey) {
        //开始解析
        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(signKey))
                .parseClaimsJws(jwt).getBody();
        return Objects.isNull(claims.getExpiration()) ||
                System.currentTimeMillis() > claims.getExpiration().getTime();
    }

}
