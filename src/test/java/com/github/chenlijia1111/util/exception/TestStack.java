package com.github.chenlijia1111.util.exception;

import com.github.chenlijia1111.utils.core.LogUtil;
import org.junit.Test;
import org.slf4j.Logger;

/**
 * @author Chen LiJia
 * @since 2019/12/28
 */
public class TestStack {

    public static Logger log = new LogUtil(TestStack.class);

    /**
     * 获取栈信息
     */
    @Test
    public void test1(){
        log.error("哈哈哈");
        String name = log.getName();
        System.out.println(name);
    }

}
