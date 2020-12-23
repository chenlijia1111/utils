package com.github.chenlijia1111.utils.core.commonCheckFunction;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 大于等于0.0的
 *
 * @author Chen LiJia
 * @since 2020/5/7
 */
public class NatureNumberCheck implements Predicate<Object> {

    @Override
    public boolean test(Object obj) {
        if (Objects.nonNull(obj)) {
            if (obj instanceof Integer) {
                return (Integer) obj >= 0;
            }
            if (obj instanceof Double) {
                return (Double) obj >= 0.0;
            }
            if (obj instanceof BigDecimal) {
                BigDecimal bigDecimal = (BigDecimal) obj;
                return bigDecimal.compareTo(BigDecimal.ZERO) >= 0;
            }
        }
        return false;
    }

}
