package com.github.chenlijia1111.utils.timer;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

/**
 * 定时器工具类
 * 写这个工具类的原因主要是
 * 虽然在 springBoot 中就有自定的定时器实现，但是使用了注解后
 * 想要在系统运行过程中灵活的控制定时器的开启与停止却不是很好
 * 所以自己用quartz写了这个工具类
 *
 * 主要的触发器有 cron 表达式触发器 和 daily 日触发器
 *
 * 主要方法为
 * @see #doTask(Trigger, Class, String, String) 启动任务
 * @see #stopTask(String, String) 停止任务
 *
 * @author 陈礼佳
 * @since 2019/9/12 22:44
 */
public class TimerTaskUtil {


    private static final Logger log = LoggerFactory.getLogger(TimerTaskUtil.class);

    /**
     * 启动一个任务
     * 注意 同一个任务组内 任务名称不允许重复 表示唯一的一个任务
     * @param trigger 触发器
     * @param jobClass 具体任务的class对象 需要实现 {@link Job} 接口
     * @param jobName 任务名称
     * @param jobGroupName 任务组名称
     */
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
