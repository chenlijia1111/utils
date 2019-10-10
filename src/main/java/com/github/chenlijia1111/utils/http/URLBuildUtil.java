package com.github.chenlijia1111.utils.http;

import com.github.chenlijia1111.utils.core.enums.CharSetType;
import com.github.chenlijia1111.utils.list.Maps;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

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
    private Map<String, String> params;


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
        params.putAll(params);
        return this;
    }


    public String paramsToString() {
        StringBuilder sb = new StringBuilder(this.baseUrl);
        if (Maps.isNotEmpty(this.params)) {
            sb.append("?");
        }

        Set<Map.Entry<String, String>> entries = params.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            sb.append(next.getKey() + "=" + next.getValue());
            if (iterator.hasNext()) {
                sb.append("&");
            }
        }

        String string = sb.toString();
        if (string.endsWith("&")) {
            string = string.substring(0, string.length() - 1);
        }
        try {
            //进行 URLEncode 编码 防止有中文
            string = URLEncoder.encode(string, CharSetType.UTF8.getType());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return string;
    }

    /**
     * 构建请求参数字符串
     * key=value&key=value
     *
     * @return
     */
    public String build() {
        StringBuilder sb = new StringBuilder();

        Set<Map.Entry<String, String>> entries = params.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            sb.append(next.getKey() + "=" + next.getValue());
            if (iterator.hasNext()) {
                sb.append("&");
            }
        }

        String string = sb.toString();
        return string;
    }
}
