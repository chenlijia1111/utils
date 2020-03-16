package com.github.chenlijia1111.utils.http;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.core.LogUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.database.redis.IRedisConnect;
import com.github.chenlijia1111.utils.encrypt.MD5EncryptUtil;
import com.github.chenlijia1111.utils.list.Lists;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * 请求重复提交校验工具 redis版本
 * 需要实现 {@link IRedisConnect}
 *
 * @author Chen LiJia
 * @since 2020/3/16
 */
public class RedisRepeatCommitCheckUtil {

    private Logger log = new LogUtil(this.getClass());

    //在1000毫秒内重复请求即表示重复请求
    public static Long repeatTimeLimit = 1000L;

    public static volatile RedisRepeatCommitCheckUtil repeatCommitCheckUtil = null;

    private IRedisConnect redisConnect;

    private RedisRepeatCommitCheckUtil() {
    }

    /**
     * 单例
     *
     * @return
     */
    public static RedisRepeatCommitCheckUtil getInstance(IRedisConnect redisConnect) {
        //校验非法参数
        AssertUtil.isTrue(Objects.nonNull(redisConnect), "redis连接为空");

        if (null == repeatCommitCheckUtil) {
            synchronized (RedisRepeatCommitCheckUtil.class) {
                if (null == repeatCommitCheckUtil) {
                    repeatCommitCheckUtil = new RedisRepeatCommitCheckUtil();
                    repeatCommitCheckUtil.redisConnect = redisConnect;
                }
            }
        }
        return repeatCommitCheckUtil;
    }

    /**
     * 根据token校验是否有重复提交数据
     *
     * @param tokenWithHeaderName token在header中的名字
     * @param request
     * @return 返回true 表明重复提交了 返回false 表明没有重复提交
     */
    public boolean checkWithToken(String tokenWithHeaderName, HttpServletRequest request) {
        return checkWithToken(tokenWithHeaderName, request, null);
    }

    /**
     * 根据token校验是否有重复提交数据
     *
     * @param tokenWithHeaderName token在header中的名字
     * @param request
     * @param methods             指定要校验的方法
     * @return 返回true 表明重复提交了 返回false 表明没有重复提交
     */
    public boolean checkWithToken(String tokenWithHeaderName, HttpServletRequest request, String... methods) {

        //md5值构建方式 token + 请求地址 + 请求类型(get/post) + 请求参数
        //token + url + method + params
        if (StringUtils.isNotEmpty(tokenWithHeaderName) && Objects.nonNull(request)) {
            String token = request.getHeader(tokenWithHeaderName);
            if (StringUtils.isEmpty(token)) {
                //没有token 直接放过
                return false;
            }
            //请求地址
            String url = request.getRequestURI();
            //请求类型
            String requestMethod = request.getMethod();
            //如果指定了校验的方法，就只校验指定的方法
            if (null != methods && !Lists.asList(methods).contains(requestMethod)) {
                return false;
            }
            //请求参数
            String params = JSONUtil.objToStr(request.getParameterMap());

            StringBuilder sb = new StringBuilder();
            sb.append(token);
            sb.append(url);
            sb.append(requestMethod);
            sb.append(params);
            //md5生成唯一值
            String md5Str = MD5EncryptUtil.MD5StringToHexString(sb.toString());
            String value = redisConnect.get(md5Str);
            Long currentDate = System.currentTimeMillis();
            //将请求记录起来
            redisConnect.putExpire(md5Str, currentDate, repeatTimeLimit);
            if (StringUtils.isNotEmpty(value) && StringUtils.isInt(value) && currentDate - Long.valueOf(value) <= repeatTimeLimit) {
                return true;
            }
        }
        return false;
    }

    /**
     * 根据token校验是否有重复提交数据
     *
     * @param request
     * @return 返回true 表明重复提交了 返回false 表明没有重复提交
     */
    public boolean checkWithSession(HttpServletRequest request) {
        return checkWithSession(request, null);
    }

    /**
     * 根据token校验是否有重复提交数据
     *
     * @param request
     * @param methods 指定要校验的方法
     * @return 返回true 表明重复提交了 返回false 表明没有重复提交
     */
    public boolean checkWithSession(HttpServletRequest request, String... methods) {

        //md5值构建方式 token + 请求地址 + 请求类型(get/post) + 请求参数
        //sessionId + url + method + params
        if (Objects.nonNull(request)) {
            String sessionId = request.getSession().getId();
            if (StringUtils.isEmpty(sessionId)) {
                //没有sessionId 直接放过
                return false;
            }
            //请求地址
            String url = request.getRequestURI();
            //请求类型
            String requestMethod = request.getMethod();
            //如果指定了校验的方法，就只校验指定的方法
            if (null != methods && !Lists.asList(methods).contains(requestMethod)) {
                return false;
            }
            //请求参数
            String params = JSONUtil.objToStr(request.getParameterMap());

            StringBuilder sb = new StringBuilder();
            sb.append(sessionId);
            sb.append(url);
            sb.append(requestMethod);
            sb.append(params);
            //md5生成唯一值
            String md5Str = MD5EncryptUtil.MD5StringToHexString(sb.toString());
            String value = redisConnect.get(md5Str);
            Long currentDate = System.currentTimeMillis();
            //将请求记录起来
            redisConnect.putExpire(md5Str, currentDate, repeatTimeLimit);
            if (StringUtils.isNotEmpty(value) && StringUtils.isInt(value) && currentDate - Long.valueOf(value) <= repeatTimeLimit) {
                return true;
            }
        }
        return false;
    }

}
