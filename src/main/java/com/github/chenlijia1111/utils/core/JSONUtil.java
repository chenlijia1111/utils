package com.github.chenlijia1111.utils.core;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.chenlijia1111.utils.common.AssertUtil;

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

    public static <T> T strToObj(String jsonStr, Class listClass, Class objClass) {

        AssertUtil.isTrue(StringUtils.isNotEmpty(jsonStr), "json字符串为空");
        AssertUtil.isTrue(Objects.nonNull(listClass), "集合class为空");
        AssertUtil.isTrue(Objects.nonNull(objClass), "对象class为空");

        ObjectMapper objectMapper = new ObjectMapper();
        JavaType javaType = objectMapper.getTypeFactory().constructParametricType(listClass, objClass);
        return null;
    }


}
