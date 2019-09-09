package com.github.chenlijia1111.util.core;

import com.github.chenlijia1111.utils.core.IDUtil;
import org.junit.Test;

/**
 * @author 陈礼佳
 * @since 2019/9/9 22:18
 */
public class IDTest {

    @Test
    public void test1(){
        IDUtil idUtil = new IDUtil(23,13);
        for (int i = 0; i < 1 << 5; i++) {
            System.out.println(idUtil.nextId());
            //1001  8
            //0000 1111 1111 1111
            //0001 0000 0000 0000
        }
    }
}
