package com.github.chenlijia1111.utils.http;

import com.github.chenlijia1111.utils.core.StringUtils;

import java.util.*;

/**
 * url 构建工具
 *
 * @author 陈礼佳
 * @since 2019/9/14 16:57
 */
public class URLBuildUtil {

    /**
     * 请求路径前缀
     */
    private String baseUrl;

    /**
     * 请求参数
     */
    private Map<String, Object> params;


    public URLBuildUtil(String baseUrl) {
        this.baseUrl = baseUrl;
        params = new TreeMap<>();
    }


    /**
     * 添加请求参数
     *
     * @param key
     * @param value
     * @return
     */
    public URLBuildUtil putParams(String key, String value) {
        params.put(key, value);
        return this;
    }

    /**
     * 添加请求参数
     *
     * @param params
     * @return
     */
    public URLBuildUtil putParams(Map<String, String> params) {
        this.params.putAll(params);
        return this;
    }


    public String paramsToString() {
        return paramsToString(true);
    }

    public String paramsToString(boolean ignoreNull) {
        StringBuilder sb = new StringBuilder();
        if (params.size() > 0) {
            Set<Map.Entry<String, Object>> entries = params.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                Object value = next.getValue();
                if (ignoreNull && (Objects.isNull(value) || StringUtils.isEmpty(value.toString()))) {
                    continue;
                }
                sb.append(key + "=" + value);
                if (iterator.hasNext()) {
                    sb.append("&");
                }
            }
        }
        //去掉最后一个 &
        if (sb.toString().endsWith("&")) {
            sb.delete(sb.length() - 1, sb.length());
        }
        return sb.toString();
    }

    /**
     * 构建Get请求参数字符串
     * http://127.0.0.1:80/key=value&key=value
     *
     * @return
     */
    public String build() {
        StringBuilder sb = new StringBuilder();
        sb.append(baseUrl);
        String paramsToString = paramsToString();
        if (StringUtils.isNotEmpty(paramsToString)) {
            sb.append("?");
            sb.append(paramsToString);
        }
        String string = sb.toString();
        return string;
    }
}
