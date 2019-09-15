package com.github.chenlijia1111.utils.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chenlijia1111.utils.core.enums.CharSetType;
import com.github.chenlijia1111.utils.xml.XmlUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 网络请求
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/8/20 10:19
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
        //通过 treeMap 可以很方便的进行构建签名操作
        //一般都需要把参数通过字典排序进行签名
        httpClientUtils.params = new TreeMap<>();
        httpClientUtils.headers = new HashMap<>();
        return httpClientUtils;
    }


    public Map<String, Object> getParams() {
        return params;
    }

    public Map<String, String> getHeaders() {
        return headers;
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
     * 添加请求参数
     *
     * @param map
     * @return
     */
    public HttpClientUtils putParams(Map<String, String> map) {
        if (null != map && map.size() > 0) {
            this.params.putAll(map);
        }
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
     * 添加请求头
     *
     * @param map 1
     * @author chenlijia
     * @since 上午 10:30 2019/8/20 0020
     **/
    public HttpClientUtils putHeader(Map<String, String> map) {
        if (null != map && map.size() > 0) {
            this.headers.putAll(map);
        }
        return this;
    }


    /**
     * 发送 get 请求
     * 对于 get 请求 路径上不可以包含中文 需要用URLEncode 编码一下才可以
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
            String paramsString = entries.stream().map(e -> {
                try {
                    return URLEncoder.encode(e.getKey(), CharSetType.UTF8.getType()) + "=" + URLEncoder.encode(e.getValue().toString(), CharSetType.UTF8.getType());
                } catch (UnsupportedEncodingException ex) {
                    ex.printStackTrace();
                }
                return null;
            }).collect(Collectors.joining("&"));
            url = url + "?" + paramsString;
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
            String s = EntityUtils.toString(entity, CharSetType.UTF8.getType());
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
            //判断有没有请求参数,如果有请求参数,拼接请求参数
            if (params.size() != 0) {
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairs, CharSetType.UTF8.getType());
                httpPost.setEntity(formEntity);
            }

            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity, CharSetType.UTF8.getType());
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
            String s = EntityUtils.toString(entity, CharSetType.UTF8.getType());
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap hashMap = objectMapper.readValue(s, HashMap.class);
            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 以 xml 的形式发送数据
     * 以 xml 的形式返回数据
     *
     * @param url
     * @return
     */
    public Map doPostWithXML(String url) {
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
        try {
            //xml 字符串
            String mapToXmlStr = XmlUtil.parseMapToXml(this.params);
            StringEntity stringEntity = new StringEntity(mapToXmlStr, "UTF-8");
            stringEntity.setContentType("text/xml;charset=UTF-8");
            stringEntity.setContentEncoding("UTF-8");

            httpPost.setEntity(stringEntity);

            CloseableHttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            String s = EntityUtils.toString(entity, CharSetType.UTF8.getType());
            Map<String, Object> map = XmlUtil.parseXMLToMap(new ByteArrayInputStream(s.getBytes(CharSetType.UTF8.getType())));
            return map;
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
            String s = EntityUtils.toString(entity, CharSetType.UTF8.getType());
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
            String s = EntityUtils.toString(entity, CharSetType.UTF8.getType());
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap hashMap = objectMapper.readValue(s, HashMap.class);
            return hashMap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 将参数转为 key=value&key=value的字符串形式
     *
     * @param ignoreNull 是否忽略掉参数值为空的参数
     * @return
     */
    public String paramsToString(boolean ignoreNull) {
        StringBuilder sb = new StringBuilder();
        if (params.size() > 0) {
            Set<Map.Entry<String, Object>> entries = params.entrySet();
            Iterator<Map.Entry<String, Object>> iterator = entries.iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, Object> next = iterator.next();
                String key = next.getKey();
                Object value = next.getValue();
                if (ignoreNull && Objects.isNull(value)) {
                    continue;
                }
                sb.append(key + "=" + value);
                if (iterator.hasNext()) {
                    sb.append("&");
                }
            }
        }
        return sb.toString();
    }

}
