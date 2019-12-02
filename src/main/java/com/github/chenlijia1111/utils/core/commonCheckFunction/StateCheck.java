package com.github.chenlijia1111.utils.core.commonCheckFunction;

import com.github.chenlijia1111.utils.list.Lists;

import java.util.function.Predicate;

/**
 * 校验参数state
 * 状态只能是 0, 1
 * @author chenlijia
 * @version 1.0
 * @since 2019/11/1 0001 上午 9:06
 **/
public class StateCheck implements Predicate<Integer> {


    @Override
    public boolean test(Integer integer) {
        return Lists.asList(0, 1).contains(integer);
    }
}
