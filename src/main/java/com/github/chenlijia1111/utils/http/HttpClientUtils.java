package com.github.chenlijia1111.utils.http;

import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.enums.CharSetType;
import com.github.chenlijia1111.utils.xml.XmlUtil;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.*;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.security.*;
import java.security.cert.CertificateException;
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
     * 文件参数
     * 只有post接口才支持
     */
    private Map<String, File> fileParams;

    /**
     * 文件二进制参数
     * 只有post接口才支持
     */
    private Map<String, byte[]> fileByteParams;

    /**
     * 文件二进制参数
     * 只有post接口才支持
     */
    private Map<String, InputStream> fileInputStreamParams;


    /**
     * 请求头
     *
     * @author chenlijia
     * @since 上午 10:37 2019/8/20 0020
     **/
    private Map<String, String> headers;

    /**
     * 请求工具
     */
    private HttpClient httpClient;


    /**
     * 请求的响应
     *
     * @since 下午 1:46 2019/10/18 0018
     **/
    private HttpResponse response;

    private HttpClientUtils() {
    }

    /**
     * 初始化
     *
     * @author chenlijia
     * @since 10:21 2019/8/20
     **/
    public static HttpClientUtils getInstance() {
        HttpClientUtils httpClientUtils = new HttpClientUtils();
        //通过 treeMap 可以很方便的进行构建签名操作
        //一般都需要把参数通过字典排序进行签名
        httpClientUtils.params = new TreeMap<>();
        httpClientUtils.fileParams = new HashMap<>();
        httpClientUtils.fileByteParams = new HashMap<>();
        httpClientUtils.fileInputStreamParams = new HashMap<>();
        httpClientUtils.headers = new HashMap<>();
        httpClientUtils.httpClient = HttpClients.createDefault();
        return httpClientUtils;
    }

    /**
     * 初始化 SSL httpClient
     *
     * @param sslFile  证书文件 如 apiclient_cert.p12
     * @param password 证书密码
     * @since 10:21 2019/8/20
     **/
    public static HttpClientUtils getInstanceWithSSL(File sslFile, String password) {

        try (FileInputStream inputStream = new FileInputStream(sslFile)) {
            return getInstanceWithSSL(inputStream, password);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 初始化 SSL httpClient
     *
     * @param sslFileInputStream 证书文件输入流 如 apiclient_cert.p12
     * @param password           证书密码
     * @since 10:21 2019/8/20
     **/
    public static HttpClientUtils getInstanceWithSSL(InputStream sslFileInputStream, String password) {
        HttpClientUtils httpClientUtils = new HttpClientUtils();
        //通过 treeMap 可以很方便的进行构建签名操作
        //一般都需要把参数通过字典排序进行签名
        httpClientUtils.params = new TreeMap<>();
        httpClientUtils.fileParams = new HashMap<>();
        httpClientUtils.fileByteParams = new HashMap<>();
        httpClientUtils.fileInputStreamParams = new HashMap<>();
        httpClientUtils.headers = new HashMap<>();
        httpClientUtils.httpClient = HttpClients.createDefault();

        //构建带证书的SSL请求
        try {
            KeyStore keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(sslFileInputStream, password.toCharArray());
            SSLContext sslcontext = SSLContexts.custom()
                    //忽略掉对服务器端证书的校验
                    .loadTrustMaterial((chain, authType) -> true)
                    .loadKeyMaterial(keyStore, password.toCharArray())
                    .build();
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(
                    sslcontext,
                    new String[]{"TLSv1"},
                    null,
                    SSLConnectionSocketFactory.getDefaultHostnameVerifier());

            httpClientUtils.httpClient = HttpClients.custom().setSSLSocketFactory(sslConnectionSocketFactory).build();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
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
     * 添加请求参数
     *
     * @param key   1
     * @param value 2
     * @author chenlijia
     * @since 上午 10:30 2019/8/20 0020
     **/
    public HttpClientUtils putFileParams(String key, File value) {
        this.fileParams.put(key, value);
        return this;
    }

    /**
     * 添加请求参数
     *
     * @param key   1
     * @param value 2
     * @author chenlijia
     * @since 上午 10:30 2019/8/20 0020
     **/
    public HttpClientUtils putFileByteParams(String key, byte[] value) {
        this.fileByteParams.put(key, value);
        return this;
    }

    /**
     * 添加请求参数
     *
     * @param key   1
     * @param value 2
     * @author chenlijia
     * @since 上午 10:30 2019/8/20 0020
     **/
    public HttpClientUtils putInputStreamParams(String key, InputStream value) {
        this.fileInputStreamParams.put(key, value);
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
     * 添加contentType
     *
     * @param contentType 1
     * @return com.github.chenlijia1111.utils.http.HttpClientUtils
     * @since 下午 2:07 2019/10/18 0018
     **/
    public HttpClientUtils setContentType(String contentType) {
        if (null != contentType) {
            this.headers.put(HTTP.CONTENT_TYPE, contentType);
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
    public HttpClientUtils doGet(String url) {
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
            this.response = httpClient.execute(httpGet);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }


    /**
     * 发送 post 请求
     *
     * @param url 1
     * @author chenlijia
     * @since 10:25 2019/8/20
     **/
    public HttpClientUtils doPost(String url) {

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
            //判断请求类型,首先看是否是上传文件的,然后看是不是要放在body里面进行请求的,默认为表单的形式发送
            if (fileParams.size() > 0 || fileByteParams.size() > 0 || fileInputStreamParams.size() > 0) {
                //上传文件
                MultipartEntityBuilder entityBuilder = MultipartEntityBuilder.create();
                entityBuilder.setCharset(Charset.forName("UTF-8"));
                if (fileParams.size() > 0) {
                    for (Map.Entry<String, File> fileEntry : fileParams.entrySet()) {
                        entityBuilder.addBinaryBody(fileEntry.getKey(), fileEntry.getValue());
                    }
                }
                if (fileByteParams.size() > 0) {
                    for (Map.Entry<String, byte[]> fileByteEntry : fileByteParams.entrySet()) {
                        entityBuilder.addBinaryBody(fileByteEntry.getKey(), fileByteEntry.getValue());
                    }
                }
                if (fileInputStreamParams.size() > 0) {
                    for (Map.Entry<String, InputStream> fileInputStreamEntry : fileInputStreamParams.entrySet()) {
                        entityBuilder.addBinaryBody(fileInputStreamEntry.getKey(), fileInputStreamEntry.getValue());
                    }
                }
                //添加参数
                if (params.size() > 0) {
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        entityBuilder.addTextBody(entry.getKey(), entry.getValue().toString());
                    }
                }

                HttpEntity httpEntity = entityBuilder.build();
                httpPost.setEntity(httpEntity);
            } else if (params.size() > 0 && headers.get(HTTP.CONTENT_TYPE) != null && headers.get(HTTP.CONTENT_TYPE).toLowerCase().contains("text/xml")) {
                //以xml的形式发送
                String s = XmlUtil.parseMapToXml(params);
                StringEntity stringEntity = new StringEntity(s, CharSetType.UTF8.getType());
                stringEntity.setContentEncoding(CharSetType.UTF8.getType());
                httpPost.setEntity(stringEntity);
            } else if (params.size() > 0 && headers.get(HTTP.CONTENT_TYPE) != null && headers.get(HTTP.CONTENT_TYPE).toLowerCase().contains("application/json")) {
                //以json的形式发送
                String s = JSONUtil.objToStr(params);
                StringEntity stringEntity = new StringEntity(s, CharSetType.UTF8.getType());
                stringEntity.setContentEncoding(CharSetType.UTF8.getType());
                httpPost.setEntity(stringEntity);
            } else if (params.size() > 0) {
                //以表单的形式请求
                ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairs, CharSetType.UTF8.getType());
                httpPost.setEntity(formEntity);
            }


            this.response = httpClient.execute(httpPost);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }


    /**
     * 发送 put application/json 请求
     * 发送 json数据
     *
     * @param url 1
     * @author chenlijia
     * @since 10:25 2019/8/20
     **/
    public HttpClientUtils doPut(String url) {
        HttpPut httpPut = new HttpPut(url);
        if (headers != null) {
            Set<Map.Entry<String, String>> entries = headers.entrySet();
            for (Map.Entry<String, String> entry : entries) {
                String key = entry.getKey();
                String value = entry.getValue();
                httpPut.addHeader(key, value);
            }
        }

        try {
            //判断有没有请求参数,如果有请求参数,拼接请求参数
            if (params.size() != 0) {
                //判断请求类型,看是不是要放在body里面进行请求的,默认为表单的形式发送
                if (headers.get(HTTP.CONTENT_TYPE) != null && headers.get(HTTP.CONTENT_TYPE).toLowerCase().contains("text/xml")) {
                    //以xml的形式发送
                    String s = XmlUtil.parseMapToXml(params);
                    StringEntity stringEntity = new StringEntity(s, CharSetType.UTF8.getType());
                    stringEntity.setContentEncoding(CharSetType.UTF8.getType());
                    httpPut.setEntity(stringEntity);
                } else if (headers.get(HTTP.CONTENT_TYPE) != null && headers.get(HTTP.CONTENT_TYPE).toLowerCase().contains("application/json")) {
                    //以json的形式发送
                    String s = JSONUtil.objToStr(params);
                    StringEntity stringEntity = new StringEntity(s, CharSetType.UTF8.getType());
                    stringEntity.setContentEncoding(CharSetType.UTF8.getType());
                    httpPut.setEntity(stringEntity);
                } else {
                    ArrayList<NameValuePair> nameValuePairs = new ArrayList<>();
                    for (Map.Entry<String, Object> entry : params.entrySet()) {
                        nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                    }
                    UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(nameValuePairs, CharSetType.UTF8.getType());
                    httpPut.setEntity(formEntity);
                }

            }

            this.response = httpClient.execute(httpPut);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }


    /**
     * 发送 delete 请求
     * delete 不可以发送body数据
     *
     * @param url 1
     * @author chenlijia
     * @since 10:25 2019/8/20
     **/
    public HttpClientUtils doDelete(String url) {
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
            this.response = httpClient.execute(httpDelete);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return this;
    }

    /**
     * 返回结果为字符串
     *
     * @return java.lang.String
     * @since 下午 2:12 2019/10/18 0018
     **/
    public String toString() {

        try {
            if (null != response) {
                HttpEntity entity = response.getEntity();
                String s = EntityUtils.toString(entity, CharSetType.UTF8.getType());
                return s;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * 返回结果为字符串
     *
     * @return java.lang.String
     * @since 下午 2:12 2019/10/18 0018
     **/
    public Map toMap() {
        try {
            if (null != response) {
                HttpEntity entity = response.getEntity();
                String s = EntityUtils.toString(entity, CharSetType.UTF8.getType());
                Map map = JSONUtil.strToObj(s, HashMap.class);
                return map;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List toList() {
        try {
            if (null != response) {
                HttpEntity entity = response.getEntity();
                String s = EntityUtils.toString(entity, CharSetType.UTF8.getType());
                List<Object> list = JSONUtil.strToList(s, ArrayList.class, Object.class);
                return list;
            }
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

}
