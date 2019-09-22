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
        byte[] bytes = NumberUtil.longToBytesLower(l);
        Long aLong = NumberUtil.bytesToLongLower(bytes);
        System.out.println(aLong);
    }


    @Test
    public void testInt(){

        int l = 456;
        byte[] bytes = NumberUtil.intToBytesLower(l);
        System.out.println(Arrays.toString(bytes));
        int aLong = NumberUtil.bytesToIntegerLower(bytes);
        System.out.println(aLong);

        byte[] bytes1 = NumberUtil.intToBytesLower(aLong);
        System.out.println(Arrays.toString(bytes1));

    }

    @Test
    public void test4(){
        double d = 456.789d;

        byte[] bytes = NumberUtil.doubleToBytes(d);
        double v = NumberUtil.bytesToDouble(bytes);
        System.out.println(v);
    }

    @Test
    public void test5(){
        short s = 123;

        byte[] bytes = NumberUtil.shortToBytes(s);
        short i = NumberUtil.bytesToShort(bytes);
        System.out.println(i);
    }

    @Test
    public void test6(){
        byte[] bytes = {1,2,3,4};
        System.out.println(NumberUtil.byteToBitString(bytes));
    }

}
