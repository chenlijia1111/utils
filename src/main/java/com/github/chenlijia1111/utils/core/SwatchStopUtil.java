package com.github.chenlijia1111.utils.core;

import org.slf4j.Logger;

/**
 * 观察执行时间
 *
 * @author 陈礼佳
 * @since 2020/4/4 9:04
 */
public class SwatchStopUtil {

    private static final Logger log = new LogUtil(SwatchStopUtil.class);

    private long startTime;

    private long endTime;


    public void watch() {
        startTime = System.currentTimeMillis();
    }

    public void stop() {
        endTime = System.currentTimeMillis();
    }

    public void print() {
        log.info("累计消耗时间" + (endTime - startTime) + "毫秒");
    }

    public void stopAndPrint() {
        stop();
        print();
    }

}
