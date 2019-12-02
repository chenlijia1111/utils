package com.github.chenlijia1111.utils.core.commonCheckFunction;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * 比率校验
 * 0-1
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/11/8 0008 上午 11:56
 **/
public class RatioCheck implements Predicate<Double> {

    @Override
    public boolean test(Double aDouble) {
        return Objects.nonNull(aDouble) && aDouble >= 0 && aDouble <= 1;
    }
}
