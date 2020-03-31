package com.github.chenlijia1111.util.http;

import com.github.chenlijia1111.utils.http.JsoupUtil;
import org.junit.Test;

/**
 * @author Chen LiJia
 * @since 2020/3/31
 */
public class TestJsoup {

    @Test
    public void test1(){
        String s = "<p><br></p>哈哈哈哈<img src=\"http://192.168.1.43:8090/toutoumai/system/file/downLoad?filePath=20200331/6b4b5dc0b7f8448bbb637485938f218f.jpg&amp;fileType=img\" alt=\"\">";

        String s1 = JsoupUtil.filterHtmlTags(s);
        System.out.println(s1);
    }

}
