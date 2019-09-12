package com.github.chenlijia1111.utils.timer;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 定时器工具类
 *
 * @author 陈礼佳
 * @since 2019/9/12 22:44
 */
public class TimerTaskUtil {


    private static final Logger log = LoggerFactory.getLogger(TimerTaskUtil.class);

    public static void doTask(Trigger trigger, Class<? extends Job> jobClass, String jobName, String jobGroupName) {

        AssertUtil.isTrue(Objects.nonNull(trigger), "触发器不能为空");
        AssertUtil.isTrue(Objects.nonNull(jobClass), "任务不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(jobName), "任务名称不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(jobGroupName), "任务组不能为空");

        //构建jobDetail
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(jobName, jobGroupName).build();
        //获取调度器
        Scheduler scheduler = SchedulerUtil.getScheduler();
        try {
            //设置调度的任务和触发器
            scheduler.scheduleJob(jobDetail, trigger);
            //启动 开始定时任务
            scheduler.start();
            log.info("定时任务" + jobName + "已启动");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }

    }


    /**
     * 停止任务
     *
     * @param jobName      任务名称
     * @param jobGroupName 任务组
     */
    public static void stopTask(String jobName, String jobGroupName) {
        Scheduler scheduler = SchedulerUtil.getScheduler();
        try {
            scheduler.deleteJob(new JobKey(jobName, jobGroupName));
            log.info("任务" + jobName + "已停止");
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }

}
