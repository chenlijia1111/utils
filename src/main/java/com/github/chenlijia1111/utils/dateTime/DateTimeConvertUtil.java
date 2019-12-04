package com.github.chenlijia1111.utils.dateTime;

import com.github.chenlijia1111.utils.core.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.Objects;

/**
 * 时间转换工具
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
            String s = new DateTime(date.getTime()).toString(pattern);
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
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
            Date date = dateTimeFormatter.parseDateTime(timeText).toDate();
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
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
            DateTime date = dateTimeFormatter.parseDateTime(timeText);
            return date;
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
            DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(pattern);
            LocalDate date = dateTimeFormatter.parseLocalDate(timeText);
            return date;
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
     * @param dateTime 1
     * @return org.joda.time.DateTime
     * @since 上午 10:15 2019/12/4 0004
     **/
    public static Date initStartTime(Date dateTime) {
        if (Objects.nonNull(dateTime)) {
            dateTime = new DateTime(dateTime.getTime()).withHourOfDay(0).withMinuteOfHour(0).withSecondOfMinute(0).toDate();
        }
        return dateTime;
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
        }
        return dateTime;
    }

    /**
     * 初始化时分秒为 23:59:59
     *
     * @param dateTime 1
     * @return org.joda.time.DateTime
     * @since 上午 10:15 2019/12/4 0004
     **/
    public static Date initEndTime(Date dateTime) {
        if (Objects.nonNull(dateTime)) {
            dateTime = new DateTime(dateTime.getTime()).withHourOfDay(23).withMinuteOfHour(59).withSecondOfMinute(59).toDate();
        }
        return dateTime;
    }

}
