package com.github.chenlijia1111.util.timeTask;

import com.github.chenlijia1111.utils.common.constant.TimeConstant;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * @author 陈礼佳
 * @since 2019/9/13 7:02
 */
public class MyJob implements Job {
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        System.out.println("执行了一次任务"+ LocalDateTime.now().format(DateTimeFormatter.ofPattern(TimeConstant.DATE_TIME)));
    }
}
