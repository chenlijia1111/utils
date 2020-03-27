package com.github.chenlijia1111.util.http;

import com.github.chenlijia1111.utils.core.JSONUtil;
import com.github.chenlijia1111.utils.http.PortScanUtil;
import com.github.chenlijia1111.utils.list.Lists;
import org.junit.Test;

import java.util.List;

/**
 * @author Chen LiJia
 * @since 2020/3/17
 */
public class TestScanPort {

    @Test
    public void test1() {
        List<Integer> integers = null;
        try {
            integers = PortScanUtil.scanPort("127.0.0.1", Lists.asList(22, 3306, 6379));
            System.out.println(JSONUtil.objToStr(integers));
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
