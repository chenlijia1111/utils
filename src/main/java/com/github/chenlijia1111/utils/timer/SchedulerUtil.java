package com.github.chenlijia1111.utils.timer;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 调度器工具
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/11 0011 下午 8:23
 **/
public class SchedulerUtil {


    private static Scheduler scheduler;


    /**
     * 获取任务调度器
     *
     * @return org.quartz.Scheduler
     * @since 下午 8:25 2019/9/11 0011
     **/
    public static Scheduler getScheduler() {
        if (null == scheduler) {
            try {
                scheduler = StdSchedulerFactory.getDefaultScheduler();
            } catch (SchedulerException e) {
                e.printStackTrace();
            }
        }
        return scheduler;
    }

}
