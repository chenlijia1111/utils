package com.github.chenlijia1111.util.core.json;

import com.github.chenlijia1111.utils.core.JSONUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/19 0019 上午 8:58
 **/
public class Test1 {

    @Test
    public void test2() {

        ArrayList<UserInfo> userInfos = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            userInfos.add(new UserInfo("name" + i, "age" + i));
        }

        String s = JSONUtil.objToStr(userInfos);
        System.out.println(s);

        List<UserInfo> list = JSONUtil.strToList(s, ArrayList.class, UserInfo.class);
        System.out.println(list);

    }

}
