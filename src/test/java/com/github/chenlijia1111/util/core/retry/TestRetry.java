package com.github.chenlijia1111.util.core.retry;

import com.github.chenlijia1111.utils.common.Result;
import com.github.chenlijia1111.utils.core.retry.RetryUtil;
import com.github.chenlijia1111.utils.list.Lists;
import org.junit.Test;

/**
 * @author Chen LiJia
 * @since 2020/7/29
 */
public class TestRetry {

    @Test
    public void test1(){
        RetryUtil retryUtil = new RetryUtil();
        retryUtil.setRepeatCount(10).setRepeatException(Lists.asList(Exception.class)).setProcess(() -> {
            System.out.println("重试了");
            int i = 1/0;
            Result result = new Result();
            result.setCode(200);
            return result;
        }).process();
    }

}
