package com.github.chenlijia1111.util.propertyCheck;

import com.github.chenlijia1111.utils.core.annos.PropertyCheck;
import com.github.chenlijia1111.utils.core.annos.TestAnno;
import com.sun.istack.internal.NotNull;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/9 0009 下午 2:46
 **/
@PropertyCheck(annotationClass = {TestAnno.class})
public class UserInfo {

    @TestAnno
    private String name;

    @TestAnno
    private String age;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
