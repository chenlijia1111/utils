package com.github.chenlijia1111.utils.http;

import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.core.LogUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.encrypt.MD5EncryptUtil;
import com.github.chenlijia1111.utils.list.Lists;
import org.slf4j.Logger;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 请求重复提交校验工具
 * 如果需要使用redis进行校验 原理类似 不过利用redis的自动清除会比较好用一点
 * {@link RedisRepeatCommitCheckUtil}
 *
 * @author Chen LiJia
 * @since 2020/3/16
 */
public class RepeatCommitCheckUtil {

    private Logger log = new LogUtil(this.getClass());

    //在1000毫秒内重复请求即表示重复请求
    public static Long repeatTimeLimit = 1000L;

    //循环每隔1分钟清理数据
    public static Long clearMapTime = 1 * 60 * 1000L;

    //请求集合的阈值 防止集合过大
    //如果真的在1分钟内囤积了100000的请求，按没人每秒两个请求算 100000/60/2 = 833 个用户 一秒内有833个用户请求 中小系统一般OK的了
    //如果用户量过大可以使用redis来进行控制
    public static Integer maxRequestMd5MapSize = 100000;

    //存储每个请求对应的md5值 用于判断是否重复请求
    private Map<String, Date> requestMd5Map = new HashMap<>();


    public static volatile RepeatCommitCheckUtil repeatCommitCheckUtil = null;

    private RepeatCommitCheckUtil() {
        new clearMd5Map().start();
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

        //检测阈值
        if (requestMd5Map.size() > maxRequestMd5MapSize) {
            requestMd5Map.clear();
        }

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
            Date date = requestMd5Map.get(md5Str);
            Date currentDate = new Date();
            //将请求记录起来
            requestMd5Map.put(md5Str, currentDate);
            if (Objects.nonNull(date) && currentDate.getTime() - date.getTime() <= repeatTimeLimit) {
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

        //检测阈值
        if (requestMd5Map.size() > maxRequestMd5MapSize) {
            requestMd5Map.clear();
        }

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
            Date date = requestMd5Map.get(md5Str);
            Date currentDate = new Date();
            //将请求记录起来
            requestMd5Map.put(md5Str, currentDate);
            if (Objects.nonNull(date) && currentDate.getTime() - date.getTime() <= repeatTimeLimit) {
                return true;
            }
        }
        return false;
    }


    /**
     * 清理请求记录集合 避免数据过大
     * 1分钟清理一次
     */
    private class clearMd5Map extends Thread {
        @Override
        public void run() {

            log.info("启动定时清理请求记录集合");

            while (true) {
                //当前时间
                long currentTimeMillis = System.currentTimeMillis();
                Set<String> keySet = requestMd5Map.keySet();
                Iterator<String> iterator = keySet.iterator();
                while (iterator.hasNext()) {
                    String md5Str = iterator.next();
                    Date date = requestMd5Map.get(md5Str);
                    if (currentTimeMillis - date.getTime() > repeatTimeLimit) {
                        //可以清理了
                        requestMd5Map.remove(md5Str);
                    }
                }

                //休息一分钟
                try {
                    Thread.sleep(clearMapTime);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
