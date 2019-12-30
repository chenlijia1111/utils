package com.github.chenlijia1111.util.core;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Chen LiJia
 * @since 2019/12/30
 */
public class TestAtomicInteger {

    @Test
    public void test1() {
        AtomicInteger currentNumber = new AtomicInteger(1);
        System.out.println(currentNumber.addAndGet(1));
        System.out.println(currentNumber.addAndGet(1));
        System.out.println(currentNumber.addAndGet(1));
        System.out.println(currentNumber.addAndGet(1));
        System.out.println(currentNumber.addAndGet(1));
        System.out.println(currentNumber.addAndGet(1));
        System.out.println(currentNumber.addAndGet(1));
    }

    @Test
    public void test2() {


    }

    public static void main(String[] args) {
    }

}
