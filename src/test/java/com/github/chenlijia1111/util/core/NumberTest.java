package com.github.chenlijia1111.util.core;

import com.github.chenlijia1111.utils.core.NumberUtil;
import org.junit.Test;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/9 0009 下午 6:05
 **/
public class NumberTest {

    @Test
    public void test1(){
        byte[] bytes = new byte[]{15, 2};
        System.out.println(NumberUtil.byteToHex(bytes));
    }

}
