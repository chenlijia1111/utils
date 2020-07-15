package com.github.chenlijia1111.utils.http.request;

import org.springframework.util.StreamUtils;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

/**
 * request 包装类，使 request 的 inputStream 可以多次读取
 * 必须要先执行 request 的 getParameterMap() 方法
 * 这个方法会先根据 请求类型 来解析参数 如果参数在 body 中就会去解析 body
 * 如果在 url 中就会去解析 url
 * 如果 body 被解析了 inputStream 就会被置空
 * 如果 body 没被解析 那么就还是传递的 inputStream
 * 如 传递 json 的 post 接口
 * 如 传递文件的 post 接口
 *
 * 使用方法如下：
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
 *         boolean check = RepeatCommitCheckUtil.getInstance().checkWithToken(Constants.TOKEN, requestWrapper);
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
 * @author Chen LiJia
 * @since 2020/7/6
 */
public class RequestWrapper extends HttpServletRequestWrapper {
    private byte[] requestBody = null;//用于将流保存下来
    private Map<String, String[]> parameterMap = null;//保存参数


    public RequestWrapper(HttpServletRequest request)  {
        super(request);
        try {
            //先解析参数
            parameterMap = request.getParameterMap();
            //后取流
            requestBody = StreamUtils.copyToByteArray(request.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public ServletInputStream getInputStream() throws IOException {
        //构造一个新的inputStream
        return new ServletInputStream() {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(requestBody);
            @Override
            public boolean isFinished() {
                return byteArrayInputStream.available() == 0;
            }

            @Override
            public boolean isReady() {
                return true;
            }


            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() throws IOException {
                return byteArrayInputStream.read();
            }
        };
    }

    /**
     * 注意：这个 BufferedReader 的 ready() 方法是返回 false 的
     * 所以不要用 ready 判断是否有内容
     * 直接根据 readLine 是否有值来进行判断就行了
     * @return
     * @throws IOException
     */
    @Override
    public BufferedReader getReader() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(getInputStream()));
        return reader;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return parameterMap;
    }

    @Override
    public Enumeration<String> getParameterNames() {
        Set<String> keySet = parameterMap.keySet();
        Vector<String> vector = new Vector<>(keySet);
        return vector.elements();
    }

    @Override
    public String getParameter(String name) {
        String[] values = parameterMap.get(name);
        if (null == values) {
            return null;
        }
        return values.length > 0 ? values[0] : super.getParameter(name);
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = parameterMap.get(name);
        return values != null ? values : super.getParameterValues(name);
    }
}
