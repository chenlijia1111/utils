package com.github.chenlijia1111.util.core;

import com.github.chenlijia1111.utils.common.constant.TimeConstant;
import com.github.chenlijia1111.utils.dateTime.BirthUtils;
import com.github.chenlijia1111.utils.dateTime.DateTimeConvertUtil;
import org.junit.Test;

import java.util.Date;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/4 0004 上午 9:36
 **/
public class TestBirth {

    @Test
    public void test() {
        //获取年龄
        int age = BirthUtils.age("362321199706101018");
        System.out.println(age);
        //获取星座
        String constellation = BirthUtils.constellation("362321199706101018");
        System.out.println(constellation);
        //获取生肖
        String zodiac = BirthUtils.zodiac("362321199706101018");
        System.out.println(zodiac);
    }

    @Test
    public void test2() {
        String s = DateTimeConvertUtil.dateToStr(new Date(), TimeConstant.DATE_TIME);
        System.out.println(s);
    }

}
