package com.github.chenlijia1111.util.propertyCheck;

import com.github.chenlijia1111.utils.common.CheckResult;
import com.github.chenlijia1111.utils.core.PropertyCheckUtil;
import org.junit.Test;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/9 0009 下午 2:47
 **/
public class Test1 {

    @Test
    public void test1(){

        UserInfo userInfo = new UserInfo();
        userInfo.setAge("as");

        CheckResult checkResult = PropertyCheckUtil.checkProperty(userInfo, null, null);
        System.out.println(checkResult);
    }
}
