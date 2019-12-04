package com.github.chenlijia1111.utils.core.enums;

import com.github.chenlijia1111.utils.common.constant.RegConstant;
import com.github.chenlijia1111.utils.core.commonCheckFunction.IDCardNoCheck;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * 对象属性校验类型
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/27 0027 下午 12:27
 **/
public enum PropertyCheckType {

    //没有校验
    NO_CHECK(e -> true),

    //手机号码
    MOBILE_PHONE(e -> {
        if (Objects.nonNull(e)) {
            return Pattern.matches(RegConstant.MOBILE_PHONE, e.toString());
        }
        return false;
    }),

    //邮箱
    E_MAIL(e -> {
        if (Objects.nonNull(e)) {
            return Pattern.matches(RegConstant.E_MAIL, e.toString());
        }
        return false;
    }),

    //身份证号
    ID_CARD(e -> {
        if (Objects.nonNull(e)) {
            String idNumber = e.toString();
            IDCardNoCheck idCardNoCheck = new IDCardNoCheck();
            return idCardNoCheck.test(idNumber);
        }
        return false;
    });


    PropertyCheckType(Predicate checkFunction) {
        this.checkFunction = checkFunction;
    }

    /**
     * 校验类型对应的校验方法
     *
     * @since 下午 12:45 2019/9/27 0027
     **/
    private Predicate checkFunction;

    public Predicate getCheckFunction() {
        return checkFunction;
    }

    public void setCheckFunction(Predicate checkFunction) {
        this.checkFunction = checkFunction;
    }
}
