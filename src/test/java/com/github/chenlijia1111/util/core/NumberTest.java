package com.github.chenlijia1111.util.core;

import com.github.chenlijia1111.utils.core.NumberUtil;
import org.junit.Test;

import java.util.Arrays;

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

    @Test
    public void test2(){
        float f = 12.3F;
        byte[] bytes = NumberUtil.floatToBytes(f);
        float v = NumberUtil.bytesToFloat(bytes);
        System.out.println(v);
    }

    @Test
    public void test3(){

        long l = 456L;
        byte[] bytes = NumberUtil.longToBytes(l);
        Long aLong = NumberUtil.bytesToLong(bytes);
        System.out.println(aLong);
    }


    @Test
    public void testInt(){

        int l = 456;
        byte[] bytes = NumberUtil.intToBytes(l);
        System.out.println(Arrays.toString(bytes));
        int aLong = NumberUtil.bytesToInteger(bytes);
        System.out.println(aLong);

        byte[] bytes1 = NumberUtil.intToBytes(aLong);
        System.out.println(Arrays.toString(bytes1));

    }

}
