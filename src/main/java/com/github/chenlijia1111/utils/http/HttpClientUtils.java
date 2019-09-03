package com.github.chenlijia1111.utils.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 网络请求
 *
 * @author chenlijia
 * @since 2019/8/20 10:19
 * @version 1.0
 **/
public class HttpClientUtils {


    /**
     * 请求参数
     *
     * @author chenlijia
     * @since 上午 10:37 2019/8/20 0020
     **/
    private Map<String, Object> params;


    /**
     * 请求头
     *
     * @author chenlijia
     * @since 上午 10:37 2019/8/20 0020
     **/
    private Map<String, String> headers;

    private HttpClientUtils() {
    }

    /**
     * 初始化
     *
     * @author chenlijia
     * @description TODO
     * @since 10:21 2019/8/20
     **/
    public static HttpClientUtils getInstance() {
        HttpClientUtils httpClientUtils = new HttpClientUtils();
        httpClientUtils.params = new HashMap<>();
        httpClientUtils.headers = new HashMap<>();
        return httpClientUtils;
    }


    /**
     * 添加请求参数
     *
     * @param key   1
     * @param value 2
     * @author chenlijia
     * @since 上午 10:30 2019/8/20 0020
     **/
    public HttpClientUtils putParams(String key, String value) {
        this.params.put(key, value);
        return this;
    }

    /**
     * 添加请求头
     *
     * @param key   1
     * @param value 2
     * @author chenlijia
     * @since 上午 10:30 2019/8/20 0020
     **/
    public HttpClientUtils putHeader(String key, String value) {
        this.headers.put(key, value);
        return this;
    }


    /**
     * 发送 get 请求
     *
     * @param url 1
     * @author chenlijia
     * @since 10:25 2019/8/20
     **/
    public Map doGet(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //判断有没有请求参数,如果有请求参数,拼接请求参数
        if (params.size() != 0) {
            Set<Map.Entry<String, Object>> entries = params.entrySet();
            //参数拼接成的字符串
            String paramsString = entries.stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
            url = url.endsWith("&") ? (url + paramsString) : (url + "&" + paramsString);
        }
        HttpGet httpGet = new HttpGet(url);
        if (headers != null) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                httpGet.addHeader(key, value);
            }
        }
        try {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity);
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap hashMap = objectMapper.readValue(s, HashMap.class);
            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 发送 post 请求
     *
     * @param url 1
     * @author chenlijia
     * @since 10:25 2019/8/20
     **/
    public Map doPost(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        //判断有没有请求参数,如果有请求参数,拼接请求参数
        if (params.size() != 0) {
            Set<Map.Entry<String, Object>> entries = params.entrySet();
            //参数拼接成的字符串
            String paramsString = entries.stream().map(e -> e.getKey() + "=" + e.getValue()).collect(Collectors.joining("&"));
            url = url.endsWith("&") ? (url + paramsString) : (url + "&" + paramsString);
        }
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                httpPost.addHeader(key, value);
            }
        }
        try {
            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity);
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap hashMap = objectMapper.readValue(s, HashMap.class);
            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 发送 post application/json 请求
     * 发送 json数据
     *
     * @param url 1
     * @author chenlijia
     * @since 10:25 2019/8/20
     **/
    public Map doPostWithJson(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        if (headers != null) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                httpPost.addHeader(key, value);
            }
        }

        //组装参数
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonStr = mapper.writeValueAsString(params);
            StringEntity stringEntity = new StringEntity(jsonStr, "UTF-8");
            stringEntity.setContentType("application/json");
            stringEntity.setContentEncoding("UTF-8");

            httpPost.setEntity(stringEntity);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity);
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap hashMap = objectMapper.readValue(s, HashMap.class);
            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 发送 put application/json 请求
     * 发送 json数据
     *
     * @param url 1
     * @author chenlijia
     * @since 10:25 2019/8/20
     **/
    public Map doPutWithJson(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPut httpPut = new HttpPut(url);
        if (headers != null) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                httpPut.addHeader(key, value);
            }
        }

        //组装参数
        ObjectMapper mapper = new ObjectMapper();
        try {
            String jsonStr = mapper.writeValueAsString(params);
            StringEntity stringEntity = new StringEntity(jsonStr, "UTF-8");
            stringEntity.setContentType("application/json");
            stringEntity.setContentEncoding("UTF-8");

            httpPut.setEntity(stringEntity);

            CloseableHttpResponse response = httpClient.execute(httpPut);
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity);
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap hashMap = objectMapper.readValue(s, HashMap.class);
            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 发送 delete 请求
     * 发送 json数据
     *
     * @param url 1
     * @author chenlijia
     * @since 10:25 2019/8/20
     **/
    public Map doDelete(String url) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpDelete httpDelete = new HttpDelete(url);
        if (headers != null) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                httpDelete.addHeader(key, value);
            }
        }
        try {
            CloseableHttpResponse response = httpClient.execute(httpDelete);
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity);
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap hashMap = objectMapper.readValue(s, HashMap.class);
            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
