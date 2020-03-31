package com.github.chenlijia1111.utils.cn;

import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.core.reflect.PropertyUtil;
import com.github.chenlijia1111.utils.http.JsoupUtil;
import com.github.chenlijia1111.utils.list.Lists;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 敏感词处理工具类
 *
 * @author Chen LiJia
 * @since 2020/3/23
 */
public class SensitiveWordsUtil {

    //敏感词汇集合  调用者注入敏感词
    public static List<String> sensitiveWordList = new ArrayList<>();

    /**
     * 判断是否包含敏感词汇
     *
     * @param str
     * @return
     */
    public static Result containSensitiveWord(String str) {
        return containSensitiveWord(str, false);
    }

    /**
     * 判断是否包含敏感词汇
     *
     * @param str
     * @param htmlStr 是否是html字符串
     * @return
     */
    public static Result containSensitiveWord(String str, boolean htmlStr) {
        if (StringUtils.isNotEmpty(str)) {
            //字符串是final修饰的，不可变，所以不用担心影响原数据
            String copyStr = str;
            if (htmlStr) {
                //过滤之后的字符串
                copyStr = JsoupUtil.filterHtmlTags(str);
            }
            for (int i = 0; i < sensitiveWordList.size(); i++) {
                String s = sensitiveWordList.get(i);
                if (copyStr.contains(s)) {
                    return Result.failure("不能包含敏感字符：" + s);
                }
            }
        }
        return Result.success("校验通过");
    }


    /**
     * 判断是否包含敏感词汇
     *
     * @param obj
     * @param ignoreFields 忽略校验的字段集合
     * @param htmlFields   html字段集合
     * @return
     */
    public static Result containSensitiveWord(Object obj, List<String> ignoreFields, List<String> htmlFields) {
        if (Objects.nonNull(obj)) {
            //获取所有属性
            List<Field> allFields = PropertyUtil.getAllFields(obj);
            ignoreFields = null == ignoreFields ? new ArrayList<>() : ignoreFields;
            htmlFields = null == htmlFields ? new ArrayList<>() : htmlFields;
            //只处理字符串
            if (Lists.isNotEmpty(allFields)) {
                for (int i = 0; i < allFields.size(); i++) {
                    Field field = allFields.get(i);
                    if (ignoreFields.contains(field.getName())) {
                        continue;
                    }
                    Class<?> type = field.getType();
                    if (type == String.class) {
                        //处理
                        //获取属性值
                        try {
                            Object fieldValue = PropertyUtil.getFieldValue(obj, obj.getClass(), field.getName());
                            if (Objects.nonNull(fieldValue)) {
                                String value = (String) fieldValue;
                                Result result = containSensitiveWord(value, htmlFields.contains(field.getName()));
                                if (!result.getSuccess()) {
                                    //包含了敏感字符
                                    return result;
                                }
                            }
                        } catch (NoSuchFieldException e) {
                            //没有这个属性
                        }
                    }
                }
            }
        }
        return Result.success("校验通过");
    }

    /**
     * 判断是否包含敏感词汇
     *
     * @param obj
     * @return
     */
    public static Result containSensitiveWord(Object obj) {
        return containSensitiveWord(obj, null, null);
    }

}
