package com.github.chenlijia1111.utils.core;

import com.github.chenlijia1111.utils.common.AssertUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

import java.util.Objects;

/**
 * 日志工具类
 * 装饰者模式,增强了一下log的功能,打印的时候可以打印当前log位置
 * 方便定位
 * <p>
 * 使用方法
 * private static final Logger log = new LogUtil(Person.class);
 *
 * @author Chen LiJia
 * @since 2019/12/28
 */
public class LogUtil implements Logger {

    /**
     * 统一使用sl4j 的统一门面
     * 方便切换哥哥日志系统
     */
    private Logger log;


    public LogUtil(Class currentClass) {
        AssertUtil.isTrue(Objects.nonNull(currentClass), "当前类不能为空");
        log = LoggerFactory.getLogger(currentClass);
    }

    /**
     * 利用栈信息
     * 获取当前log位置信息
     * 打log的时候会自己带上类信息
     * 所以补充行数,方便定位
     *
     * @return
     */
    private StringBuilder currentLocationInfo() {
        //方法栈
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        //0下标是当前位置 1就是调用本方法的位置 2才是真实方法的位置
        StackTraceElement stackTraceElement = stackTrace[2];
        StringBuilder stringBuilder = new StringBuilder();
        //时间
        stringBuilder.append("时间：" + DateTime.now().toString());
        stringBuilder.append(" ");
        stringBuilder.append(stackTraceElement.getClassName());
        stringBuilder.append(".");
        stringBuilder.append(stackTraceElement.getMethodName());
        stringBuilder.append(" 行数:");
        stringBuilder.append(stackTraceElement.getLineNumber());
        stringBuilder.append(" ; log内容:");
        return stringBuilder;
    }

    /**
     * 返回打log的类信息
     *
     * @return
     */
    @Override
    public String getName() {
        return log.getName();
    }

    @Override
    public boolean isTraceEnabled() {
        return log.isTraceEnabled();
    }

    @Override
    public void trace(String s) {
        log.trace(currentLocationInfo().append(s).toString());
    }

    @Override
    public void trace(String s, Object o) {
        log.trace(currentLocationInfo().append(s).toString(), o);
    }

    @Override
    public void trace(String s, Object o, Object o1) {
        log.trace(currentLocationInfo().append(s).toString(), o, o1);
    }

    @Override
    public void trace(String s, Object... objects) {
        log.trace(currentLocationInfo().append(s).toString(), objects);
    }

    @Override
    public void trace(String s, Throwable throwable) {
        log.trace(currentLocationInfo().append(s).toString(), throwable);
    }

    @Override
    public boolean isTraceEnabled(Marker marker) {
        return log.isTraceEnabled(marker);
    }

    @Override
    public void trace(Marker marker, String s) {
        log.trace(marker, currentLocationInfo().append(s).toString());
    }

    @Override
    public void trace(Marker marker, String s, Object o) {
        log.trace(marker, currentLocationInfo().append(s).toString(), o);
    }

    @Override
    public void trace(Marker marker, String s, Object o, Object o1) {
        log.trace(marker, currentLocationInfo().append(s).toString(), o, o1);
    }

    @Override
    public void trace(Marker marker, String s, Object... objects) {
        log.trace(marker, currentLocationInfo().append(s).toString(), objects);
    }

    @Override
    public void trace(Marker marker, String s, Throwable throwable) {
        log.trace(marker, currentLocationInfo().append(s).toString(), throwable);
    }

    @Override
    public boolean isDebugEnabled() {
        return log.isDebugEnabled();
    }

    @Override
    public void debug(String s) {
        log.debug(currentLocationInfo().append(s).toString());
    }

    @Override
    public void debug(String s, Object o) {
        log.debug(currentLocationInfo().append(s).toString(), o);
    }

    @Override
    public void debug(String s, Object o, Object o1) {
        log.debug(currentLocationInfo().append(s).toString(), o, o1);
    }

    @Override
    public void debug(String s, Object... objects) {
        log.debug(currentLocationInfo().append(s).toString(), objects);
    }

    @Override
    public void debug(String s, Throwable throwable) {
        log.debug(currentLocationInfo().append(s).toString(), throwable);
    }

    @Override
    public boolean isDebugEnabled(Marker marker) {
        return log.isDebugEnabled(marker);
    }

    @Override
    public void debug(Marker marker, String s) {
        log.debug(marker, currentLocationInfo().append(s).toString());
    }

    @Override
    public void debug(Marker marker, String s, Object o) {
        log.debug(marker, currentLocationInfo().append(s).toString(), o);
    }

    @Override
    public void debug(Marker marker, String s, Object o, Object o1) {
        log.debug(marker, currentLocationInfo().append(s).toString(), o, o1);
    }

    @Override
    public void debug(Marker marker, String s, Object... objects) {
        log.debug(marker, currentLocationInfo().append(s).toString(), objects);
    }

    @Override
    public void debug(Marker marker, String s, Throwable throwable) {
        log.debug(marker, currentLocationInfo().append(s).toString(), throwable);
    }

    @Override
    public boolean isInfoEnabled() {
        return log.isInfoEnabled();
    }

    @Override
    public void info(String s) {
        log.info(currentLocationInfo().append(s).toString());
    }

    @Override
    public void info(String s, Object o) {
        log.info(currentLocationInfo().append(s).toString(), o);
    }

    @Override
    public void info(String s, Object o, Object o1) {
        log.info(currentLocationInfo().append(s).toString(), o, o1);
    }

    @Override
    public void info(String s, Object... objects) {
        log.info(currentLocationInfo().append(s).toString(), objects);
    }

    @Override
    public void info(String s, Throwable throwable) {
        log.info(currentLocationInfo().append(s).toString(), throwable);
    }

    @Override
    public boolean isInfoEnabled(Marker marker) {
        return log.isInfoEnabled(marker);
    }

    @Override
    public void info(Marker marker, String s) {
        log.info(marker, currentLocationInfo().append(s).toString());
    }

    @Override
    public void info(Marker marker, String s, Object o) {
        log.info(marker, currentLocationInfo().append(s).toString(), o);
    }

    @Override
    public void info(Marker marker, String s, Object o, Object o1) {
        log.info(marker, currentLocationInfo().append(s).toString(), o, o1);
    }

    @Override
    public void info(Marker marker, String s, Object... objects) {
        log.info(marker, currentLocationInfo().append(s).toString(), objects);
    }

    @Override
    public void info(Marker marker, String s, Throwable throwable) {
        log.info(marker, currentLocationInfo().append(s).toString(), throwable);
    }

    @Override
    public boolean isWarnEnabled() {
        return log.isWarnEnabled();
    }

    @Override
    public void warn(String s) {
        log.warn(currentLocationInfo().append(s).toString());
    }

    @Override
    public void warn(String s, Object o) {
        log.warn(currentLocationInfo().append(s).toString(), o);
    }

    @Override
    public void warn(String s, Object... objects) {
        log.warn(currentLocationInfo().append(s).toString(), objects);
    }

    @Override
    public void warn(String s, Object o, Object o1) {
        log.warn(currentLocationInfo().append(s).toString(), o, o1);
    }

    @Override
    public void warn(String s, Throwable throwable) {
        log.warn(currentLocationInfo().append(s).toString(), throwable);
    }

    @Override
    public boolean isWarnEnabled(Marker marker) {
        return log.isWarnEnabled(marker);
    }

    @Override
    public void warn(Marker marker, String s) {
        log.warn(marker, currentLocationInfo().append(s).toString());
    }

    @Override
    public void warn(Marker marker, String s, Object o) {
        log.warn(marker, currentLocationInfo().append(s).toString(), o);
    }

    @Override
    public void warn(Marker marker, String s, Object o, Object o1) {
        log.warn(marker, currentLocationInfo().append(s).toString(), o, o1);
    }

    @Override
    public void warn(Marker marker, String s, Object... objects) {
        log.warn(marker, currentLocationInfo().append(s).toString(), objects);
    }

    @Override
    public void warn(Marker marker, String s, Throwable throwable) {
        log.warn(marker, currentLocationInfo().append(s).toString(), throwable);
    }

    @Override
    public boolean isErrorEnabled() {
        return log.isErrorEnabled();
    }

    @Override
    public void error(String s) {
        log.error(currentLocationInfo().append(s).toString());
    }

    @Override
    public void error(String s, Object o) {
        log.error(currentLocationInfo().append(s).toString(), o);
    }

    @Override
    public void error(String s, Object o, Object o1) {
        log.error(currentLocationInfo().append(s).toString(), o, o1);
    }

    @Override
    public void error(String s, Object... objects) {
        log.error(currentLocationInfo().append(s).toString(), objects);
    }

    @Override
    public void error(String s, Throwable throwable) {
        log.error(currentLocationInfo().append(s).toString(), throwable);
    }

    @Override
    public boolean isErrorEnabled(Marker marker) {
        return log.isErrorEnabled(marker);
    }

    @Override
    public void error(Marker marker, String s) {
        log.error(marker, currentLocationInfo().append(s).toString());
    }

    @Override
    public void error(Marker marker, String s, Object o) {
        log.error(marker, currentLocationInfo().append(s).toString(), o);
    }

    @Override
    public void error(Marker marker, String s, Object o, Object o1) {
        log.error(marker, currentLocationInfo().append(s).toString(), o, o1);
    }

    @Override
    public void error(Marker marker, String s, Object... objects) {
        log.error(marker, currentLocationInfo().append(s).toString(), objects);
    }

    @Override
    public void error(Marker marker, String s, Throwable throwable) {
        log.error(marker, currentLocationInfo().append(s).toString(), throwable);
    }
}
