package com.github.chenlijia1111.utils.http;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.IOUtil;
import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.database.redis.IRedisConnect;
import com.github.chenlijia1111.utils.encrypt.MD5EncryptUtil;
import com.github.chenlijia1111.utils.list.Lists;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Objects;

/**
 * 请求重复提交校验工具 redis版本
 * 需要实现 {@link IRedisConnect}
 *
 * 详细说明见 {@link RepeatCommitCheckUtil}
 *
 * @author Chen LiJia
 * @since 2020/3/16
 */
public class RedisRepeatCommitCheckUtil {


    //在 200 毫秒内重复请求即表示重复请求
    public static Long repeatTimeLimit = 200L;

    //是否要拦截未登录用户
    public static boolean filterNotLogin = false;

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
     * @return 返回true 没有重复提交 返回false 表明重复提交了
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
     * @return 返回true 没有重复提交 返回false 表明重复提交了
     */
    public boolean checkWithToken(String tokenWithHeaderName, HttpServletRequest request, String... methods) {

        //md5值构建方式 ip + token + 请求地址 + 请求类型(get/post) + 请求参数
        //ip + token + url + method + params
        if (StringUtils.isNotEmpty(tokenWithHeaderName) && Objects.nonNull(request)) {
            //ip地址
            String ipAddr = HttpUtils.getIpAddr(request);

            String token = request.getHeader(tokenWithHeaderName);
            if (StringUtils.isEmpty(token)) {
                //没有token
                if(filterNotLogin){
                    //要拦截未登录用户，token 赋值空字符串
                    token = "";
                }else {
                    //不拦截未登陆用户
                    return true;
                }
            }
            //请求地址
            String url = request.getRequestURI();
            //请求类型
            String requestMethod = request.getMethod();
            //如果指定了校验的方法，就只校验指定的方法
            if (null != methods && methods.length > 0 && !Lists.asList(methods).contains(requestMethod)) {
                return true;
            }
            //请求参数
            String params = requestToParams(request);

            StringBuilder sb = new StringBuilder();
            sb.append(ipAddr);
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
            if (StringUtils.isNotEmpty(value) && StringUtils.isInt(value) && currentDate - Long.valueOf(value) > repeatTimeLimit) {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取请求参数 params + body
     * @param request
     * @return
     */
    private String requestToParams(HttpServletRequest request){
        StringBuilder sb = new StringBuilder();
        String params = JSONUtil.objToStr(request.getParameterMap());
        sb.append(params);
        try {
            ServletInputStream inputStream = request.getInputStream();
            if(Objects.nonNull(inputStream)){
                String s = IOUtil.readToString(inputStream);
                sb.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 根据 session 校验是否有重复提交数据
     *
     * @param request
     * @return 返回true 没有重复提交 返回false 表明重复提交了
     */
    public boolean checkWithSession(HttpServletRequest request) {
        return checkWithSession(request, null);
    }

    /**
     * 根据 session 校验是否有重复提交数据
     *
     * @param request
     * @param methods 指定要校验的方法
     * @return 返回true 没有重复提交 返回false 表明重复提交了
     */
    public boolean checkWithSession(HttpServletRequest request, String... methods) {

        //md5值构建方式 ip + token + 请求地址 + 请求类型(get/post) + 请求参数
        //ip + sessionId + url + method + params
        if (Objects.nonNull(request)) {
            //ip
            String ipAddr = HttpUtils.getIpAddr(request);

            String sessionId = request.getSession().getId();
            if (StringUtils.isEmpty(sessionId)) {
                //没有sessionId
                if(filterNotLogin){
                    //要拦截未登录用户，sessionId 赋值空字符串
                    sessionId = "";
                }else {
                    //不拦截未登陆用户
                    return true;
                }
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
            String params = requestToParams(request);

            StringBuilder sb = new StringBuilder();
            sb.append(ipAddr);
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
            if (StringUtils.isNotEmpty(value) && StringUtils.isInt(value) && currentDate - Long.valueOf(value) > repeatTimeLimit) {
                return true;
            }
        }
        return false;
    }

}
