package com.github.chenlijia1111.util.core;

import com.github.chenlijia1111.utils.cn.TranslatorUtil;
import com.github.chenlijia1111.utils.cn.enums.TranslateLanguageEnum;
import com.github.chenlijia1111.utils.core.IOUtil;
import org.junit.Test;

import java.io.File;

/**
 * @author Chen LiJia
 * @since 2019/12/18
 */
public class IOTest {

    @Test
    public void test1() {
        for (int i = 0; i < 1; i++) {
            String translate = TranslatorUtil.translate("中国", TranslateLanguageEnum.ZH_CN, TranslateLanguageEnum.EN);
            System.out.println(translate);
        }
    }

}
