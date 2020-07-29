package com.github.chenlijia1111.utils.core.retry;

import com.github.chenlijia1111.utils.common.Result;

/**
 * 重试执行的具体操作
 *
 * @author Chen LiJia
 * @since 2020/7/29
 */
public interface IRetryProcess {

    /**
     * 具体执行的操作
     *
     * @return
     */
    Result process();

}
