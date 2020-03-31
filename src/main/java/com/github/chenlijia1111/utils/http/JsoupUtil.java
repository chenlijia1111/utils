package com.github.chenlijia1111.utils.http;

import com.github.chenlijia1111.utils.core.StringUtils;
import org.jsoup.Jsoup;

/**
 * jsoup工具
 *
 * @author Chen LiJia
 * @since 2020/3/31
 */
public class JsoupUtil {

    /**
     * 过滤 html 标签
     * 比如 img 标签，他会把 img 标签整个去除，只保留标签与标签之间的文本
     * 适合做：
     * 敏感词处理
     * 全文检索，只需要检索内容，要过滤标签
     * @param str
     * @return
     */
    public static String filterHtmlTags(String str) {
        if (StringUtils.isEmpty(str)) {
            return str;
        }

        String text = Jsoup.parse(str).text();
        return text;
    }

}
