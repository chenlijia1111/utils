package com.github.chenlijia1111.utils.dateTime;

import com.github.chenlijia1111.utils.core.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;

import java.util.Date;
import java.util.Objects;

/**
 * 时间转换工具
 * <p>
 * 实话说,java 内置的LocalDateTime真的不是很好用,想跟原有的,感觉跟之前的Date 之间一点都不友好
 * 用过之后简直难以忍受,比如 LocalDateTime 居然不可以格式化 yyyy-MM-dd
 * 想要转化 yyyy-MM-dd 就必须使用 LocalDate 分的这么清,真是很难受啊
 * 还是joda.time 好用
 * <p>
 * 计算时间间隔
 * {@link org.joda.time.Years}
 * {@link org.joda.time.Months}
 * {@link org.joda.time.Days}
 * {@link org.joda.time.Minutes}
 * {@link org.joda.time.Seconds}
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/12/4 0004 上午 10:02
 **/
public class DateTimeConvertUtil {

    /**
     * 时间转字符串
     *
     * @param date    时间
     * @param pattern 转化的格式 {@link com.github.chenlijia1111.utils.common.constant.TimeConstant}
     * @return java.lang.String
     * @since 上午 10:08 2019/12/4 0004
     **/
    public static String dateToStr(Date date, String pattern) {
        if (Objects.nonNull(date) && StringUtils.isNotEmpty(pattern)) {
            String s = new DateTime(date).toString(pattern);
            return s;
        }
        return null;
    }

    /**
     * 字符串转时间
     *
     * @param timeText 时间字符串
     * @param pattern  转化的格式 {@link com.github.chenlijia1111.utils.common.constant.TimeConstant}
     * @return java.util.Date
     * @since 上午 10:11 2019/12/4 0004
     **/
    public static Date strToDate(String timeText, String pattern) {
        if (StringUtils.isNotEmpty(timeText) && StringUtils.isNotEmpty(pattern)) {
            Date date = DateTimeFormat.forPattern(pattern).parseDateTime(timeText).toDate();
            return date;
        }
        return null;
    }

    /**
     * 字符串转时间
     *
     * @param timeText 时间字符串
     * @param pattern  转化的格式 {@link com.github.chenlijia1111.utils.common.constant.TimeConstant}
     * @return org.joda.time.DateTime
     * @since 上午 10:11 2019/12/4 0004
     **/
    public static DateTime strToDateTime(String timeText, String pattern) {
        if (StringUtils.isNotEmpty(timeText) && StringUtils.isNotEmpty(pattern)) {
            DateTime dateTime = DateTimeFormat.forPattern(pattern).parseDateTime(timeText);
            return dateTime;
        }
        return null;
    }

    /**
     * 字符串转时间
     *
     * @param timeText 时间字符串
     * @param pattern  转化的格式 {@link com.github.chenlijia1111.utils.common.constant.TimeConstant}
     * @return org.joda.time.LocalDate
     * @since 上午 10:11 2019/12/4 0004
     **/
    public static LocalDate strToLocalDate(String timeText, String pattern) {
        if (StringUtils.isNotEmpty(timeText) && StringUtils.isNotEmpty(pattern)) {
            LocalDate localDate = DateTimeFormat.forPattern(pattern).parseDateTime(timeText).toLocalDate();
            return localDate;
        }
        return null;
    }

    /**
     * 初始化时分秒为 00:00:00
     *
     * @param dateTime 1
     * @return org.joda.time.DateTime
     * @since 上午 10:15 2019/12/4 0004
     **/
    public static DateTime initStartTime(DateTime dateTime) {
        if (Objects.nonNull(dateTime)) {
            dateTime = dateTime.withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0);
        }
        return dateTime;
    }

    /**
     * 初始化时分秒为 00:00:00
     *
     * @param date 1
     * @return org.joda.time.DateTime
     * @since 上午 10:15 2019/12/4 0004
     **/
    public static Date initStartTime(Date date) {
        if (Objects.nonNull(date)) {
            return initStartTime(new DateTime(date.getTime())).toDate();
        }
        return null;
    }

    /**
     * 初始化时分秒为 23:59:59
     *
     * @param dateTime 1
     * @return org.joda.time.DateTime
     * @since 上午 10:15 2019/12/4 0004
     **/
    public static DateTime initEndTime(DateTime dateTime) {
        if (Objects.nonNull(dateTime)) {
            dateTime = dateTime.withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59);
            return dateTime;
        }
        return null;
    }

    /**
     * 初始化时分秒为 23:59:59
     *
     * @param date 1
     * @return org.joda.time.DateTime
     * @since 上午 10:15 2019/12/4 0004
     **/
    public static Date initEndTime(Date date) {
        if (Objects.nonNull(date)) {
            return initEndTime(new DateTime(date.getTime())).toDate();
        }
        return null;
    }

}
