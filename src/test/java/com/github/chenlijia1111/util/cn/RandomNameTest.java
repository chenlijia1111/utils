package com.github.chenlijia1111.util.cn;

import com.github.chenlijia1111.utils.cn.RandomNameUtil;
import org.junit.Test;

/**
 * @author Chen LiJia
 * @since 2020/3/10
 */
public class RandomNameTest {

    @Test
    public void test1() {
        for (int i = 0; i < 2000; i++) {
            System.out.println(RandomNameUtil.randomName(true, 3));
        }
    }
    @Test
    public void test2() {
        for (int i = 0; i < 2000; i++) {
            System.out.println(RandomNameUtil.randomName("蔡", 3));
        }
    }


}
