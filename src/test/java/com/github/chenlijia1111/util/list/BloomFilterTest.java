package com.github.chenlijia1111.util.list;

import com.github.chenlijia1111.utils.core.SwatchStopUtil;
import com.github.chenlijia1111.utils.list.BloomFilterUtil;
import org.junit.Test;

/**
 * @author 陈礼佳
 * @since 2020/4/4 9:03
 */
public class BloomFilterTest {

    @Test
    public void test1(){
        SwatchStopUtil swatchStopUtil = new SwatchStopUtil();
        swatchStopUtil.watch();
        String email = "zhenlingcn@126.com";
        BloomFilterUtil bloomDemo = new BloomFilterUtil();
        System.out.println(email + "是否在列表中： " + bloomDemo.contains(email));
        bloomDemo.add(email);
        System.out.println(email + "是否在列表中： " + bloomDemo.contains(email));
        email = "zhenlingcn@163.com";
        System.out.println(email + "是否在列表中： " + bloomDemo.contains(email));
        swatchStopUtil.stopAndPrint();
    }

}
