package com.github.chenlijia1111.utils.core;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.github.chenlijia1111.utils.common.AssertUtil;

import java.io.IOException;
import java.util.HashMap;
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
     * 单例初始化
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    /**
     * 对象转json
     *
     * @param obj
     * @return
     */
    public static String objToStr(Object obj) {
        if (Objects.nonNull(obj)) {
            try {
                String jsonStr = objectMapper.writer().writeValueAsString(obj);
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

        ObjectReader objectReader = objectMapper.reader();
        try {
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(jsonStr);
            T t = objectReader.readValue(jsonParser, objClass);
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

        ObjectReader objectReader = objectMapper.reader();
        try {
            JsonNode jsonNode = objectReader.readTree(jsonStr);
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

        ObjectReader objectReader = objectMapper.reader();
        JavaType javaType = objectReader.getTypeFactory().constructParametricType(listClass, objClass);
        try {
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(jsonStr);
            List<T> list = objectReader.readValue(jsonParser, javaType);
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
    public static <K, V> Map<K, V> strToMap(String jsonStr, Class<? extends Map> mapClass, Class<K> keyClass, Class<V> valueClass) {

        AssertUtil.isTrue(StringUtils.isNotEmpty(jsonStr), "json字符串为空");
        AssertUtil.isTrue(Objects.nonNull(mapClass), "集合class为空");
        AssertUtil.isTrue(Objects.nonNull(keyClass), "key class为空");
        AssertUtil.isTrue(Objects.nonNull(valueClass), "value class为空");

        ObjectReader objectReader = objectMapper.reader();
        JavaType javaType = objectReader.getTypeFactory().constructParametricType(mapClass, keyClass, valueClass);
        try {
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(jsonStr);
            Map<K, V> map = objectReader.readValue(jsonParser, javaType);
            return map;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 对象 转 map
     * <p>
     * 先将对象转为 json 之后，再将 json 转为 map
     *
     * @param object   1
     * @param mapClass 2
     * @return java.util.List<T>
     * @since 上午 9:10 2019/9/19 0019
     **/
    public static Map objToMap(Object object, Class<? extends Map> mapClass) {

        AssertUtil.isTrue(Objects.nonNull(object), "对象为空");
        AssertUtil.isTrue(Objects.nonNull(mapClass), "集合class为空");

        // 先将对象转为 json
        String json = objToStr(object);
        // 再将 json 转为 map
        Map<Object, Object> map = strToMap(json, HashMap.class, Object.class, Object.class);
        return map;
    }


}
