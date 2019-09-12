package com.github.chenlijia1111.util.timeTask;

import com.github.chenlijia1111.utils.timer.TimerTaskUtil;
import com.github.chenlijia1111.utils.timer.TriggerUtil;
import org.junit.Test;
import org.quartz.*;

import java.time.LocalTime;

/**
 * 定时任务测试
 *
 * @author 陈礼佳
 * @since 2019/9/12 23:15
 */
public class TimeTaskTest {

    @Test
    public void test1() {

        Trigger trigger = TriggerUtil.createCronTrigger("* * * * * ?", "trigger1", "triggerGroupName1");

        TimerTaskUtil.doTask(trigger, MyJob.class, "jobName", "jobGroupName");


        try {
            Thread.sleep(10000);
            TimerTaskUtil.stopTask("jobName", "jobGroupName");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Test
    public void test2(){
        Trigger trigger = TriggerUtil.createDailyTrigger(LocalTime.of(7, 0), LocalTime.of(7, 14),
                "triggerName", "triggerGroupName",
                DateBuilder.IntervalUnit.SECOND, 1);

        TimerTaskUtil.doTask(trigger, MyJob.class, "jobName", "jobGroupName");


    }


    public static void main(String[] args) {
        TimeTaskTest test = new TimeTaskTest();
        test.test2();
    }



}
