package com.github.chenlijia1111.utils.dateTime;

import com.github.chenlijia1111.utils.core.StringUtils;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

/**
 * 时间转换工具
 * 计算时间间隔 {@link Period}
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
            LocalDateTime localDateTime = dateToLocalDateTime(date);
            String s = DateTimeFormatter.ofPattern(pattern).format(localDateTime);
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
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime localDateTime = LocalDateTime.parse(timeText, dateTimeFormatter);
            Date date = localDateTimeToDate(localDateTime);
            return date;
        }
        return null;
    }

    /**
     * Date 转 LocalDateTime
     *
     * @param date
     * @return
     */
    public static LocalDateTime dateToLocalDateTime(Date date) {
        if (Objects.nonNull(date)) {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(date.getTime()), ZoneId.systemDefault());
            return localDateTime;
        }
        return null;
    }

    /**
     * localDateTime 转为 Date
     *
     * @param localDateTime
     * @return
     */
    public static Date localDateTimeToDate(LocalDateTime localDateTime) {
        if (Objects.nonNull(localDateTime)) {
            ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
            Date date = Date.from(zonedDateTime.toInstant());
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
    public static LocalDateTime strToDateTime(String timeText, String pattern) {
        if (StringUtils.isNotEmpty(timeText) && StringUtils.isNotEmpty(pattern)) {
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            LocalDateTime localDateTime = LocalDateTime.parse(timeText, dateTimeFormatter);
            return localDateTime;
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
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(pattern);
            LocalDate localDate = LocalDate.parse(timeText, dateTimeFormatter);
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
    public static LocalDateTime initStartTime(LocalDateTime dateTime) {
        if (Objects.nonNull(dateTime)) {
            dateTime = dateTime.withHour(0).withMinute(0).withSecond(0);
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
            LocalDateTime localDateTime = dateToLocalDateTime(date).withHour(0).withMinute(0).withSecond(0);
            return localDateTimeToDate(localDateTime);
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
    public static LocalDateTime initEndTime(LocalDateTime dateTime) {
        if (Objects.nonNull(dateTime)) {
            dateTime = dateTime.withHour(23).withMinute(59).withSecond(59);
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
            LocalDateTime localDateTime = dateToLocalDateTime(date).withHour(23).withMinute(59).withSecond(59);
            return localDateTimeToDate(localDateTime);
        }
        return null;
    }

}
