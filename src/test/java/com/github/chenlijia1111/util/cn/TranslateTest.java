package com.github.chenlijia1111.util.cn;

import com.github.chenlijia1111.utils.cn.TranslatorUtil;
import com.github.chenlijia1111.utils.cn.enums.TranslateLanguageEnum;
import com.github.chenlijia1111.utils.common.Result;
import org.junit.Test;

/**
 * 测试翻译
 * @author 陈礼佳
 * @since 2019/11/30 10:40
 */
public class TranslateTest {

    @Test
    public void test1(){
        String translate = TranslatorUtil.translate("我是一个中国人", TranslateLanguageEnum.ZH_CN, TranslateLanguageEnum.EN);
        System.out.println(translate);
    }

    @Test
    public void test2(){
        Result.resultLanguage = TranslateLanguageEnum.EN.getAbbr();
        System.out.println(Result.failure("参数不合法"));
    }
}
