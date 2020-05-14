package com.github.chenlijia1111.utils.core.commonCheckFunction;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * 大于等于0.0的小数
 * @author Chen LiJia
 * @since 2020/5/7
 */
public class NatureNumberCheck implements Predicate<Double> {

    @Override
    public boolean test(Double aDouble) {
        return Objects.nonNull(aDouble) && aDouble >= 0.0;
    }
}
