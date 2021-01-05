package com.github.chenlijia1111.utils.core.retry.holdTarget;

import com.github.chenlijia1111.utils.common.AssertUtil;
import org.joda.time.DateTime;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 延时对象
 *
 * @author Chen LiJia
 * @since 2021/1/5
 */
public class DelayRetryWrapperVo implements Delayed {

    /**
     * 对应的调用的对象
     */
    private Object target;

    /**
     * 方法的参数
     */
    private Object[] args;

    /**
     * 方法对象
     */
    private Method method;

    /**
     * 延时时间
     * 单位：秒
     */
    private Integer delaySeconds;

    /**
     * 出队时间
     * 时间戳
     */
    private long limitTime;

    /**
     * 已重试次数
     * 默认为 0
     */
    private Integer retryCount = 0;

    /**
     * 构造方法
     *
     * @param target       目标调用对象
     * @param args         方法参数 可以为空
     * @param method       方法
     * @param delaySeconds 延时时间-秒
     */
    public DelayRetryWrapperVo(Object target, Object[] args, Method method, Integer delaySeconds) {

        AssertUtil.notNull(target, "目标调用对象不能为空");
        AssertUtil.notNull(method, "方法不能为空");

        this.target = target;
        this.args = args;
        this.method = method;
        this.delaySeconds = delaySeconds;
        if (Objects.nonNull(delaySeconds)) {
            this.limitTime = DateTime.now().plusSeconds(delaySeconds).getMillis();
        } else {
            // 没传，直接立即出队
            this.limitTime = System.currentTimeMillis();
        }
    }

    /**
     * 核心方法，根据这个方法来延时等待
     * 延时的时间是根据时间差来进行返回的
     *
     * @param unit
     * @return
     */
    @Override
    public long getDelay(TimeUnit unit) {
        // 毫秒数 进行比较
        return limitTime - System.currentTimeMillis();
    }

    /**
     * 通过这个比较判断谁最先出队
     * 默认是从小到大排序的
     *
     * @param o
     * @return
     */
    @Override
    public int compareTo(Delayed o) {
        DelayRetryWrapperVo that = (DelayRetryWrapperVo) o;
        long l = this.limitTime - that.limitTime;
        return Long.valueOf(l).intValue();
    }

    public Object getTarget() {
        return target;
    }

    public Object[] getArgs() {
        return args;
    }

    public Method getMethod() {
        return method;
    }

    public Integer getDelaySeconds() {
        return delaySeconds;
    }

    public long getLimitTime() {
        return limitTime;
    }

    public Integer getRetryCount() {
        return retryCount;
    }

    public void setDelaySeconds(Integer delaySeconds) {
        this.delaySeconds = delaySeconds;
        if (Objects.nonNull(delaySeconds)) {
            this.limitTime = DateTime.now().plusSeconds(delaySeconds).getMillis();
        } else {
            // 没传，直接立即出队
            this.limitTime = System.currentTimeMillis();
        }
    }

    /**
     * 增加重试次数
     */
    public void increseRetryCount(){
        this.retryCount = this.retryCount + 1;
    }
}
