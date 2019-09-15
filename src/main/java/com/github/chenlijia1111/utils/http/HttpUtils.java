package com.github.chenlijia1111.utils.http;

import com.github.chenlijia1111.utils.core.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @author chenlijia
 * @since 2018/12/14 0014
 */
public class HttpUtils {


    //获取请求Ip
    public static String getIpAddr(HttpServletRequest request) {

        if(Objects.isNull(request)){
            return "127.0.0.1";
        }

        String ip = request.getHeader("x-forwarded-for");
        if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
            // 多次反向代理后会有多个ip值，第一个ip才是真实ip
            if (ip.indexOf(",") != -1) {
                ip = ip.split(",")[0];
            }
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip.equals("0:0:0:0:0:0:0:1") ? "127.0.0.1" : ip;
    }


    /**
     * 判断是否IE浏览器
     *
     * @param request
     * @return
     */
    public static boolean isIE(HttpServletRequest request) {

        String[] IEBrowserSignals = {"MSIE", "Trident", "Edge"};
        String userAgent = request.getHeader("User-Agent");
        for (String signal : IEBrowserSignals) {
            if (userAgent.contains(signal)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 获取 http://192.168.1.134:8082/expertise
     *
     * @param request 1
     * @return java.lang.String
     * @author chenlijia
     * @since 下午 6:01 2019/5/16 0016
     **/
    public static String currentRequestUrlPrefix(HttpServletRequest request) {
        String requestUrl = request.getScheme() //当前链接使用的协议
                + "://" + request.getServerName()//服务器地址
                + ":" + request.getServerPort() //端口号
                + request.getContextPath(); //应用名称，如果应用名称为

        return requestUrl;
    }


    /**
     * 获取 url 中的参数
     *
     * @param url       1
     * @param paramsKry 2
     * @return java.lang.String
     * @author chenlijia
     * @since 下午 2:54 2019/6/14 0014
     **/
    public static String parseUrlParams(String url, String paramsKry) {
        if (StringUtils.isEmpty(url))
            return null;

        String[] split = url.split("\\?");
        if (split.length < 2)
            return null;
        String params = split[1];
        String[] param = params.split("&");
        if (param.length == 0)
            return null;
        for (String s : param) {
            String[] split1 = s.split("=");
            if (split1[0].equals(paramsKry) && split1.length == 2)
                return split1[1];
        }
        return null;
    }

}
