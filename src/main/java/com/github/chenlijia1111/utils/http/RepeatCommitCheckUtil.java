package com.github.chenlijia1111.utils.http;

import com.github.chenlijia1111.utils.core.IOUtil;
import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.cache.CacheObject;
import com.github.chenlijia1111.utils.core.cache.CacheUtil;
import com.github.chenlijia1111.utils.core.cache.TimeOutTypeEnum;
import com.github.chenlijia1111.utils.encrypt.MD5EncryptUtil;
import com.github.chenlijia1111.utils.list.Lists;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
 *
 * 另外：一般对于不需要用户登陆即可访问的接口，不需要做校验，
 * 因为可能会有同一个外网下的局域网用于访问，这样有可能拦截到不应该拦截的请求
 * 所以默认如果用户未登陆，不拦截接口
 *
 *
 * 注意：因为这里读取了 request 的输入流，后面的流程如果还需要使用输入流就会造成输入流不可用，必须自定义实现 request 包装
 * {@link com.github.chenlijia1111.utils.http.request.RequestWrapper}
 *
 * {@code
 *      @WebFilter(filterName = "myFilter", urlPatterns = "/*")
 * public class MyFilter implements Filter {
 *
 *     private Logger log = new LogUtil(this.getClass());
 *
 *     @Override
 *     public void init(FilterConfig filterConfig) throws ServletException {
 *         log.info("myFilter 跨域过滤器初始化");
 *     }
 *
 *     @Override
 *     public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
 *
 *         HttpServletRequest request = (HttpServletRequest) servletRequest;
 *         HttpServletResponse response = (HttpServletResponse) servletResponse;
 *
 *         //包装 request
 *         RequestWrapper requestWrapper = new RequestWrapper(request);
 *
 *         //拦截所有请求，判断请求间隔
 *         boolean check = RepeatCommitCheckUtil.getInstance().checkWithToken(Constants.TOKEN, request);
 *         if (!check) {
 *             ResponseUtil.printRest(Result.failure("请求频率过于频繁，请稍后进行操作"), response);
 *             return;
 *         }
 *
 *         filterChain.doFilter(requestWrapper, servletResponse);
 *     }
 *
 *     @Override
 *     public void destroy() {
 *
 *     }
 * }
 * }
 *
 * @author Chen LiJia
 * @since 2020/3/16
 */
public class RepeatCommitCheckUtil {

    //在 200 毫秒内重复请求即表示重复请求
    public static Long repeatTimeLimit = 200L;

    //单例
    private static volatile RepeatCommitCheckUtil repeatCommitCheckUtil = null;

    //是否要拦截未登录用户
    public static boolean filterNotLogin = false;

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

        //md5值构建方式 ip +  token + 请求地址 + 请求类型(get/post) + 请求参数
        //ip + token + url + method + params
        if (StringUtils.isNotEmpty(tokenWithHeaderName) && Objects.nonNull(request)) {
            //ip 地址
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
            //如果指定了校验的方法，就只校验指定的方法  统一比较小写
            if (null != methods && methods.length > 0 && !Lists.asList(methods).stream().map(e -> e.toLowerCase()).collect(Collectors.toList()).contains(requestMethod.toLowerCase())) {
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
     * @param methods 指定要校验的方法
     * @return 返回true 没有重复提交 返回false 表明重复提交了
     */
    public boolean checkWithSession(HttpServletRequest request, String... methods) {

        //md5值构建方式 ip + sessionId + 请求地址 + 请求类型(get/post) + 请求参数
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
            if (null != methods && methods.length > 0 && !Lists.asList(methods).contains(requestMethod)) {
                return true;
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
