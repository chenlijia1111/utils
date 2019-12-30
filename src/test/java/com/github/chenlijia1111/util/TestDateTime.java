package com.github.chenlijia1111.util;

import org.junit.Test;

import java.time.LocalDate;

/**
 * @author Chen LiJia
 * @since 2019/12/30
 */
public class TestDateTime {

    @Test
    public void test1(){
        int dayOfMonth = LocalDate.now().getDayOfMonth();
        System.out.println(dayOfMonth);
    }

}
