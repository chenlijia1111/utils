package com.github.chenlijia1111.utils.core.commonCheckFunction;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * 价格校验
 * 必须大于0
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/11/1 0001 下午 2:05
 **/
public class PriceCheck implements Predicate<Double> {


    @Override
    public boolean test(Double d) {
        return Objects.nonNull(d) && d >= 0.0;
    }
}
