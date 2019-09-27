package com.github.chenlijia1111.util.propertyCheck;

import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.core.PropertyCheckUtil;
import org.junit.Test;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/27 0027 下午 1:17
 **/
public class Test1 {

    @Test
    public void test1() {

        UserInfo userInfo = new UserInfo("陈丽佳", "571740367@qq.com", "362321199706101018","17770039942");
        Result result = PropertyCheckUtil.checkProperty(userInfo);
        System.out.println(result);
    }


}
