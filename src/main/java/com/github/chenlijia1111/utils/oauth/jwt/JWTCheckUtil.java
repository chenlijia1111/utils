package com.github.chenlijia1111.utils.oauth.jwt;

import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.core.StringUtils;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.joda.time.DateTime;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

/**
 * jwt 校验工具
 *
 * @author 陈礼佳
 * @since 2019/12/22 12:51
 */
public class JWTCheckUtil {

    /**
     * 过期时间 默认 24小时
     * 默认续期一天
     */
    private Integer expireSeconds = 24 * 60 * 60;

    /**
     * 刷新时间 默认 2小时
     * 表示间隔两个小时以上刷新
     */
    private Integer refreshSeconds = 2 * 60 * 60;

    /**
     * 老token失效缓冲期
     * 如果刚好处在老旧token更替的时间
     * 明明已经获取了新的token,却由于并发问题
     * 导致前端携带的还是老的token
     * 所以这里给一个老token的失效缓冲器
     * 默认在过期后的30秒还是可以使用
     */
    private Integer bufferExpireSeconds = 30;

    /**
     * 校验token
     * 当一个请求进来时，解析他获取token的时间，如果像个两个小时以上并且token没有过期
     * 那个就可以刷新一个新的token给前端
     * 放在response的token里
     * <p>
     * 假想场景,如果一个页面有多个请求同时请求后台,这个时候又都是token刷新期,都需要刷新token
     * 那么第一个请求进来,获取到新的token返回去,第二个也进来,拿着旧的token返回去,第三个也是如此
     * 也就是说只要token没过期,就不影响,只是这几次都需要经过换新的token,可能会有点慢
     * <p>
     * 但是,如果是快过期的前一秒都多个请求过来,就可能会有问题了,前面的请求来了,获取到了新的token,但是
     * 后面的几个请求还是拿的之前的token来请求,那么这个时候就会判断token已经失效了
     * 这个问题需不需要解决,还要看系统的要求，如果要求不高
     * 这样基本就行了
     * 但是如果要求高的话，可以给老的token 增加缓冲期,默认是在30秒之后还是有效的
     * {@link #bufferExpireSeconds}
     *
     * @param jwtToken
     * @param jwtSignKey jwt签名加密密钥
     * @return 当刷新成功的时候, data里会返回新的token
     */
    public Result checkToken(String jwtToken, String jwtSignKey) {
        if (StringUtils.isEmpty(jwtToken)) {
            return Result.failure("token为空");
        }

        if (StringUtils.isEmpty(jwtSignKey)) {
            return Result.failure("jwt签名加密密钥为空");
        }


        //解析token
        Claims claims = JWTUtil.parseJWT(jwtToken, jwtSignKey);
        //获取token 的时间
        Date issuedAt = claims.getIssuedAt();
        //过期时间
        Date expiration = claims.getExpiration();
        //判断是否过期
        long currentTimeMillis = System.currentTimeMillis();
        if (expiration.getTime() + bufferExpireSeconds * 1000 <= currentTimeMillis) {
            return Result.failure("token已过期");
        }

        //判断是否超过了刷新时间,如果超过了,就生成新的token
        if ((currentTimeMillis - issuedAt.getTime()) > refreshSeconds * 1000L) {
            //刷新token
            //设置新的获取token时间
            claims.setIssuedAt(new Date(currentTimeMillis));
            //设置新的token过期时间
            claims.setExpiration(new DateTime(currentTimeMillis).plusSeconds(expireSeconds).toDate());
            //生成新的token
            //签名类型 SHA-256
            SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
            //创建用于签名的密钥
            byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtSignKey);
            Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
            String newToken = Jwts.builder().addClaims(claims).signWith(signatureAlgorithm, signingKey).compact();
            return Result.success("校验成功,刷新token成功", newToken);
        }
        return Result.success("校验成功,无需刷新token");
    }


    public void setExpireSeconds(Integer expireSeconds) {
        this.expireSeconds = expireSeconds;
    }

    public void setRefreshSeconds(Integer refreshSeconds) {
        this.refreshSeconds = refreshSeconds;
    }

    public void setBufferExpireSeconds(Integer bufferExpireSeconds) {
        this.bufferExpireSeconds = bufferExpireSeconds;
    }
}
