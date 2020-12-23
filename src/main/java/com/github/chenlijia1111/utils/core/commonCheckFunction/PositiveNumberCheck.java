package com.github.chenlijia1111.utils.core.commonCheckFunction;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * 正数校验
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/11/5 0005 下午 5:03
 **/
public class PositiveNumberCheck implements Predicate<Object> {


    @Override
    public boolean test(Object obj) {
        if (Objects.nonNull(obj)) {
            if (obj instanceof Integer) {
                return (Integer) obj > 0;
            }
            if (obj instanceof Double) {
                return (Double) obj > 0.0;
            }
            if (obj instanceof BigDecimal) {
                BigDecimal bigDecimal = (BigDecimal) obj;
                return bigDecimal.compareTo(BigDecimal.ZERO) > 0;
            }
        }
        return false;
    }
}
