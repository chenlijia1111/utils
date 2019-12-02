package com.github.chenlijia1111.utils.core.commonCheckFunction;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * 正数校验
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/11/5 0005 下午 5:03
 **/
public class PositiveNumberCheck implements Predicate<Integer> {


    @Override
    public boolean test(Integer integer) {
        return Objects.nonNull(integer) && integer > 0;
    }
}
