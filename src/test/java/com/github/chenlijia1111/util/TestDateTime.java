package com.github.chenlijia1111.util;

import org.junit.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * @author Chen LiJia
 * @since 2019/12/30
 */
public class TestDateTime {

    @Test
    public void test1() {
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        System.out.println(dayOfMonth);
    }

    @Test
    public void test2() {
        String s = "2019-01-01";
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate ldt = LocalDate.parse(s, df);
        System.out.println("String类型的时间转成LocalDateTime：" + ldt);


    }

}
