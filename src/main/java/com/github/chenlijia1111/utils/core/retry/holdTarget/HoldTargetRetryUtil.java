package com.github.chenlijia1111.utils.core.retry.holdTarget;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.DelayQueue;

/**
 * 重试工具
 *
 * @author Chen LiJia
 * @since 2021/1/5
 */
public class HoldTargetRetryUtil {

    // 延时队列
    private DelayQueue<DelayRetryWrapperVo> delayQueue = new DelayQueue<>();

    // 线程变量
    public static final ThreadLocal<DelayRetryWrapperVo> DELAY_THREAD_LOCAL = new ThreadLocal<>();

    private static volatile HoldTargetRetryUtil instance;

    /**
     * 私有构造
     */
    private HoldTargetRetryUtil() {
        // 启动消费线程
        new ConsumeThread().start();
    }

    /**
     * 单例
     * @return
     */
    public static HoldTargetRetryUtil getInstance() {
        if (Objects.isNull(instance)) {
            synchronized (HoldTargetRetryUtil.class) {
                if (Objects.isNull(instance)) {
                    instance = new HoldTargetRetryUtil();
                }
            }
        }
        return instance;
    }

    /**
     * 添加到延时队列
     *
     * @param vo
     */
    public void add(DelayRetryWrapperVo vo) {
        if (Objects.nonNull(vo)) {
            delayQueue.put(vo);
        }
    }

    /**
     * 消费线程
     */
    private class ConsumeThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    DelayRetryWrapperVo vo = delayQueue.take();
                    // 重新执行
                    Object target = vo.getTarget();
                    Method method = vo.getMethod();
                    Object[] args = vo.getArgs();

                    // 将 delay 对象放入线程变量
                    DELAY_THREAD_LOCAL.set(vo);
                    // 反射调用方法
                    method.invoke(target, args);
                    // 执行完了，增加已重试次数
                    vo.increseRetryCount();

                    // 是否需要接着重试由客户端方法进行判断
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
