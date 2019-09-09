package com.github.chenlijia1111.util.core;

import com.github.chenlijia1111.utils.cn.ChineseNumberUtil;
import org.junit.Test;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/9 0009 下午 6:07
 **/
public class ChineseNumberTest {

    @Test
    public void test1() {
        System.out.println(ChineseNumberUtil.getChineseNumber(1994));
        System.out.println(ChineseNumberUtil.getChineseNumber(1994.1115));
        System.out.println(ChineseNumberUtil.getChineseNumber(19941115));
    }

}
