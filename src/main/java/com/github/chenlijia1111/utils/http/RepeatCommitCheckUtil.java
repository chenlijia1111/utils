package com.github.chenlijia1111.utils.http;

import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.cache.CacheObject;
import com.github.chenlijia1111.utils.core.cache.CacheUtil;
import com.github.chenlijia1111.utils.core.cache.TimeOutTypeEnum;
import com.github.chenlijia1111.utils.encrypt.MD5EncryptUtil;
import com.github.chenlijia1111.utils.list.Lists;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 请求重复提交校验工具
 * 如果需要使用redis进行校验 原理类似 不过利用redis的自动清除会比较好用一点
 * {@link RedisRepeatCommitCheckUtil}
 *
 *
 * 为了防止恶意请求，比如，用户提现，恶意请求发出，通过脚本发出，同一时刻，发起大量提现请求
 * 即使后台做了处理，很有小部分可能就会出现数据不一致的问题。
 *
 * 加了请求间隔限制之后就可以给到后台处理的一个缓冲时间
 *
 * 一般给到一个请求 200ms 即可
 *
 * @author Chen LiJia
 * @since 2020/3/16
 */
public class RepeatCommitCheckUtil {

    //在 200 毫秒内重复请求即表示重复请求
    public static Long repeatTimeLimit = 200L;

    //单例
    private static volatile RepeatCommitCheckUtil repeatCommitCheckUtil = null;

    private RepeatCommitCheckUtil() {

    }

    /**
     * 单例
     *
     * @return
     */
    public static RepeatCommitCheckUtil getInstance() {
        if (null == repeatCommitCheckUtil) {
            synchronized (RepeatCommitCheckUtil.class) {
                if (null == repeatCommitCheckUtil) {
                    repeatCommitCheckUtil = new RepeatCommitCheckUtil();
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
     * @param methods             指定要校验的方法
     * @return 返回true 没有重复提交 返回false 表明重复提交了
     */
    public boolean checkWithToken(String tokenWithHeaderName, HttpServletRequest request, String... methods) {

        //md5值构建方式 token + 请求地址 + 请求类型(get/post) + 请求参数
        //token + url + method + params
        if (StringUtils.isNotEmpty(tokenWithHeaderName) && Objects.nonNull(request)) {
            String token = request.getHeader(tokenWithHeaderName);
            if (StringUtils.isEmpty(token)) {
                //没有token 赋值空字符串
                token = "";
            }
            //请求地址
            String url = request.getRequestURI();
            //请求类型
            String requestMethod = request.getMethod();
            //如果指定了校验的方法，就只校验指定的方法  统一比较小写
            if (null != methods && methods.length > 0 && !Lists.asList(methods).stream().map(e -> e.toLowerCase()).collect(Collectors.toList()).contains(requestMethod.toLowerCase())) {
                return true;
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

            CacheUtil cacheUtil = CacheUtil.getInstance();
            CacheObject cacheObject = cacheUtil.get(md5Str);
            if (Objects.nonNull(cacheObject)) {
                //说明这个请求存在
                //并且还没有被清理掉
                return false;
            }

            cacheObject = new CacheObject(md5Str, null, repeatTimeLimit, TimeOutTypeEnum.FIXED_TIME_OUT);
            cacheUtil.put(cacheObject);
            return true;
        }
        return false;
    }


    /**
     * 根据 session 校验是否有重复提交数据
     *
     * @param request
     * @param methods 指定要校验的方法
     * @return 返回true 没有重复提交 返回false 表明重复提交了
     */
    public boolean checkWithSession(HttpServletRequest request, String... methods) {

        //md5值构建方式 sessionId + 请求地址 + 请求类型(get/post) + 请求参数
        //sessionId + url + method + params
        if (Objects.nonNull(request)) {
            String sessionId = request.getSession().getId();
            if (StringUtils.isEmpty(sessionId)) {
                //没有sessionId 赋值空字符串
                sessionId = "";
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
            String params = JSONUtil.objToStr(request.getParameterMap());

            StringBuilder sb = new StringBuilder();
            sb.append(sessionId);
            sb.append(url);
            sb.append(requestMethod);
            sb.append(params);
            //md5生成唯一值
            String md5Str = MD5EncryptUtil.MD5StringToHexString(sb.toString());

            CacheUtil cacheUtil = CacheUtil.getInstance();
            CacheObject cacheObject = cacheUtil.get(md5Str);
            if (Objects.nonNull(cacheObject)) {
                //说明这个请求存在
                //并且还没有被清理掉
                return false;
            }

            cacheObject = new CacheObject(md5Str, null, repeatTimeLimit, TimeOutTypeEnum.FIXED_TIME_OUT);
            cacheUtil.put(cacheObject);
            return true;
        }
        return false;
    }

}
