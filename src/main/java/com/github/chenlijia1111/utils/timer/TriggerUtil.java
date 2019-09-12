package com.github.chenlijia1111.utils.timer;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.core.StringUtils;
import com.github.chenlijia1111.utils.list.Sets;
import org.quartz.*;
import org.quartz.impl.triggers.DailyTimeIntervalTriggerImpl;

import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

/**
 * 触发器工具类
 * 表达式触发器
 * 日期触发器
 * <p>
 * 触发器的groupName 主要是用于分组
 * 相当于java的包名的概念相似
 * 判断是否是同一个触发器就是通过判断 触发器的组名和触发器名称 是否都一致
 *
 * @author 陈礼佳
 * @since 2019/9/12 21:46
 */
public class TriggerUtil {


    /**
     * 创建表达式触发器
     * corn从左到右（用空格隔开）：秒 分 小时 月份中的日期 月份 星期中的日期 年份
     * ? 可以用在 月份中的日期 和 星期中的日期 表示任意的值
     * 年份可以省略
     *
     * @param cronStr     表达式字符串
     * @param triggerName 触发器名称
     * @param groupName   触发器组名称
     * @return
     */
    public static Trigger createCronTrigger(String cronStr, String triggerName, String groupName) {

        AssertUtil.isTrue(StringUtils.isNotEmpty(cronStr), "定时器表达式不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(triggerName), "触发器名称不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(groupName), "触发器组名称不能为空");

        //判断系统中是否已经有这个触发器了,如果有,直接取出使用
        Trigger exists = checkExists(triggerName, groupName);
        if (Objects.nonNull(exists)) {
            return exists;
        }

        CronScheduleBuilder builder = CronScheduleBuilder.cronSchedule(cronStr);
        CronTrigger trigger = TriggerBuilder.newTrigger().withSchedule(builder).
                withIdentity(triggerName, groupName).
                startNow().build();
        return trigger;
    }


    /**
     * 创建日触发器
     * 每天的某个时间点开始启动
     * 每天的某个时间点停止
     *
     * @param startTime      每天的开始时间
     * @param endTime        每天的结束时间
     * @param triggerName    触发器名称
     * @param groupName      触发器组名称
     * @param repeatUnit     重复时间单位 {@link DateBuilder.IntervalUnit}
     * @param repeatInterval 重复时间单位的数量 如 repeatUnit 为 {@link DateBuilder.IntervalUnit#MINUTE}
     *                       repeatInterval 为5 则代表 5分钟执行一次定时器
     * @return
     */
    public static Trigger createDailyTrigger(LocalTime startTime, LocalTime endTime,
                                             String triggerName, String groupName,
                                             DateBuilder.IntervalUnit repeatUnit, int repeatInterval) {
        return createDailyTrigger(startTime, endTime, triggerName, groupName,
                repeatUnit, repeatInterval, null);
    }


    /**
     * 创建日触发器
     * 每天的某个时间点开始启动
     * 每天的某个时间点停止
     *
     * @param startTime      每天的开始时间
     * @param endTime        每天的结束时间
     * @param triggerName    触发器名称
     * @param groupName      触发器组名称
     * @param repeatUnit     重复时间单位 {@link DateBuilder.IntervalUnit}
     * @param repeatInterval 重复时间单位的数量 如 repeatUnit 为 {@link DateBuilder.IntervalUnit#MINUTE}
     *                       repeatInterval 为5 则代表 5分钟执行一次定时器
     * @param dayOfWeekSet   在一个星期内的哪些天执行
     * @return
     */
    public static Trigger createDailyTrigger(LocalTime startTime, LocalTime endTime,
                                             String triggerName, String groupName,
                                             DateBuilder.IntervalUnit repeatUnit, int repeatInterval,
                                             Set<Integer> dayOfWeekSet) {

        AssertUtil.isTrue(Objects.nonNull(startTime), "每天的开始时间不能为空");
        AssertUtil.isTrue(Objects.nonNull(endTime), "每天的结束时间不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(triggerName), "触发器名称不能为空");
        AssertUtil.isTrue(StringUtils.isNotEmpty(groupName), "触发器组名称不能为空");

        //判断系统中是否已经有这个触发器了,如果有,直接取出使用
        Trigger exists = checkExists(triggerName, groupName);
        if (Objects.nonNull(exists)) {
            return exists;
        }

        DailyTimeIntervalTriggerImpl trigger = new DailyTimeIntervalTriggerImpl();

        trigger.setName(triggerName);
        trigger.setGroup(groupName);
        trigger.setStartTimeOfDay(new TimeOfDay(startTime.getHour(), startTime.getMinute(), startTime.getSecond()));
        trigger.setEndTimeOfDay(new TimeOfDay(endTime.getHour(), endTime.getMinute(), endTime.getSecond()));
        trigger.setRepeatIntervalUnit(repeatUnit);
        trigger.setRepeatInterval(repeatInterval);
        //在一周的某些天运行,如只在工作日运行
        if (Sets.isNotEmpty(dayOfWeekSet)) {
            trigger.setDaysOfWeek(dayOfWeekSet);
        }
        return trigger;
    }


    /**
     * 判断系统中是否已经存在了这个触发器
     * 存在就直接取出
     *
     * @param triggerName 触发器名称
     * @param groupName   触发器组
     * @return
     */
    private static Trigger checkExists(String triggerName, String groupName) {
        //判断系统中是否已经有这个触发器了,如果有,直接取出使用
        TriggerKey triggerKey = new TriggerKey(triggerName, groupName);
        Scheduler scheduler = SchedulerUtil.getScheduler();
        try {
            boolean b = scheduler.checkExists(triggerKey);
            if (b) {
                Trigger trigger = scheduler.getTrigger(triggerKey);
                return trigger;
            }
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
        return null;
    }

}
