package com.github.chenlijia1111.utils.cn;

import com.github.chenlijia1111.utils.cn.enums.TranslateLanguageEnum;
import com.github.chenlijia1111.utils.http.HttpClientUtils;
import com.github.chenlijia1111.utils.list.Lists;

import java.util.List;
import java.util.Objects;

/**
 * 翻译工具类
 * 借助谷歌翻译
 *
 * @author 陈礼佳
 * @since 2019/11/30 10:07
 */
public class TranslatorUtil {

    /**
     * 语言翻译
     *
     * @param word         要翻译的内容
     * @param fromLanguage 当前内容语言
     * @param toLanguage   要翻译的语言
     * @return
     */
    public static String translate(String word, String fromLanguage, String toLanguage) {
        List list = HttpClientUtils.getInstance().
                putParams("client", "gtx").
                putParams("sl", fromLanguage).
                putParams("tl", toLanguage).
                putParams("hl", "zh-CN").
                putParams("dt", "t").
                putParams("q", word).
                doGet("https://translate.google.cn/translate_a/single").toList();
        System.out.println(list);
        //[[[I love China, 我爱中国, null, null, 1]], null, zh-CN]
        if (Lists.isNotEmpty(list)) {
            List o = (List) list.get(0);
            if (Lists.isNotEmpty(o)) {
                List o1 = (List) o.get(0);
                if (Lists.isNotEmpty(o1)) {
                    Object o2 = o1.get(0);
                    if (Objects.nonNull(o2)) {
                        return o2.toString();
                    }
                }

            }
        }
        return null;
    }

    /**
     * 语言翻译
     *
     * @param word         要翻译的内容
     * @param fromLanguage 当前内容语言
     * @param toLanguage   要翻译的语言
     * @return
     */
    public static String translate(String word, TranslateLanguageEnum fromLanguage,
                                   TranslateLanguageEnum toLanguage) {
        return translate(word, fromLanguage.getAbbr(), toLanguage.getAbbr());
    }

}
