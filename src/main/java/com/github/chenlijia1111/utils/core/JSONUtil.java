package com.github.chenlijia1111.utils.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chenlijia1111.utils.common.AssertUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * json 操作工具类
 *
 * @author 陈礼佳
 * @since 2019/9/18 23:11
 */
public class JSONUtil {


    /**
     * 对象转json
     *
     * @param obj
     * @return
     */
    public static String objToStr(Object obj) {
        if (Objects.nonNull(obj)) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String jsonStr = objectMapper.writeValueAsString(obj);
                return jsonStr;
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * json 转 对象
     *
     * @param jsonStr  1
     * @param objClass 2
     * @return T
     * @since 上午 9:09 2019/9/19 0019
     **/
    public static <T> T strToObj(String jsonStr, Class<T> objClass) {

        AssertUtil.isTrue(StringUtils.isNotEmpty(jsonStr), "json字符串为空");
        AssertUtil.isTrue(Objects.nonNull(objClass), "对象class为空");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            T t = objectMapper.readValue(jsonStr, objClass);
            return t;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * json 转 jsonNode对象
     *
     * @param jsonStr 1
     * @return T
     * @since 上午 9:09 2019/9/19 0019
     **/
    public static JsonNode strToJsonNode(String jsonStr) {

        AssertUtil.isTrue(StringUtils.isNotEmpty(jsonStr), "json字符串为空");

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(jsonStr);
            return jsonNode;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * json 转集合
     *
     * @param jsonStr   1
     * @param listClass 2
     * @param objClass  3
     * @return java.util.List<T>
     * @since 上午 9:10 2019/9/19 0019
     **/
    public static <T> List<T> strToList(String jsonStr, Class<? extends List> listClass, Class<T> objClass) {

        AssertUtil.isTrue(StringUtils.isNotEmpty(jsonStr), "json字符串为空");
        AssertUtil.isTrue(Objects.nonNull(listClass), "集合class为空");
        AssertUtil.isTrue(Objects.nonNull(objClass), "对象class为空");

        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(listClass, objClass);
        try {
            List<T> list = objectMapper.readValue(jsonStr, javaType);
            return list;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * json 转map
     *
     * @param jsonStr    1
     * @param mapClass   2
     * @param keyClass   3
     * @param valueClass 3
     * @return java.util.List<T>
     * @since 上午 9:10 2019/9/19 0019
     **/
    public static <K, V> Map<K, V> strToMap(String jsonStr, Class<? extends Map<K, V>> mapClass, Class<K> keyClass, Class<V> valueClass) {

        AssertUtil.isTrue(StringUtils.isNotEmpty(jsonStr), "json字符串为空");
        AssertUtil.isTrue(Objects.nonNull(mapClass), "集合class为空");
        AssertUtil.isTrue(Objects.nonNull(keyClass), "key class为空");
        AssertUtil.isTrue(Objects.nonNull(valueClass), "value class为空");

        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(mapClass, keyClass, valueClass);
        try {
            Map<K, V> map = objectMapper.readValue(jsonStr, javaType);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
