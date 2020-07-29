package com.github.chenlijia1111.utils.core.retry;

import com.github.chenlijia1111.utils.common.AssertUtil;
import com.github.chenlijia1111.utils.common.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 重试工具类
 * <p>
 * 要素：
 * 重试的条件->具体的执行内容->执行结果/异常->根据重试的条件判断是否需要重试->
 *
 * 可以根据异常来进行重试，也可以根据返回结果的状态码 {@link Result#getCode()} 是否等于 {@link RetryUtil#RETRY_RESULT_CODE} 来进行判断
 *
 * 使用场景1：发送邮件，发送完之后，判断是否发送成功，如果发送失败，返回状态码 {@link RetryUtil#RETRY_RESULT_CODE} 或者根据异常进行捕获 然后就会进行重试发送
 * 使用场景2: 12306 抢票，开始执行之后，判断票量是否充足，如果不足，返回状态码 {@link RetryUtil#RETRY_RESULT_CODE} 然后就会进行重试抢票
 *
 * @author Chen LiJia
 * @since 2020/7/29
 */
public class RetryUtil {

    /**
     * 重复--永远
     */
    private static int REPEAT_FOREVER = -1;

    /**
     * 指定需要重试的 result 的状态码
     */
    private static int RETRY_RESULT_CODE = 67;

    /**
     * 当前重试次数
     * 默认-1次
     * 即第一次不算重试的数量
     * 第二次就算重试了一次
     */
    private int currentRepeatCount = -1;

    /**
     * 配置最多重试的次数
     * 默认为0 即不重试
     */
    private Integer repeatCount = 0;

    /**
     * 配置需要重试的异常
     */
    private List<Class> repeatException = new ArrayList<>();

    /**
     * 配置需要执行的具体的操作
     */
    private IRetryProcess process;

    public RetryUtil setRepeatCount(Integer repeatCount) {
        if (Objects.isNull(repeatCount)) {
            repeatCount = 0;
        }
        this.repeatCount = repeatCount;
        return this;
    }

    public RetryUtil setRepeatException(List<Class> repeatException) {
        if (Objects.isNull(repeatException)) {
            repeatException = new ArrayList<>();
        }
        this.repeatException = repeatException;
        return this;
    }

    public RetryUtil setProcess(IRetryProcess process) {
        AssertUtil.isTrue(Objects.nonNull(process), "执行内容不能为空");
        this.process = process;
        return this;
    }

    /**
     * 开始执行内容
     *
     * @return
     */
    public Result process() {
        AssertUtil.isTrue(Objects.nonNull(this.process), "执行内容不能为空");
        Result result = null;
        while (true) {
            try {
                //当前重试次数
                currentRepeatCount++;
                result = this.process.process();

                //正常执行结束
                //判断是否需要重试
                if (!(Objects.nonNull(result) && Objects.equals(RETRY_RESULT_CODE, result.getCode()) &&
                        (Objects.equals(REPEAT_FOREVER, repeatCount) || currentRepeatCount < repeatCount))) {
                    //不需要重试了
                    break;
                }
            } catch (Exception e) {
                //异常还是要打印的，不然不知道是什么异常
                e.printStackTrace();
                //判断异常
                Class<? extends Exception> currentExceptionClass = e.getClass();
                boolean checkRepeatExceptionCondition = checkRepeatExceptionCondition(currentExceptionClass);
                if (!checkRepeatExceptionCondition) {
                    //不需要重试了，跳出循环
                    break;
                }
            }
        }
        return result;
    }

    /**
     * 根据条件判断是否需要重试
     *
     * @return
     */
    private boolean checkRepeatExceptionCondition(Class<? extends Exception> currentExceptionClass) {
        for (Class exceptionClass : repeatException) {
            if (Objects.equals(currentExceptionClass, exceptionClass) || exceptionClass.isAssignableFrom(currentExceptionClass)) {
                //满足捕获条件，需要重试
                if (Objects.equals(REPEAT_FOREVER, repeatCount) || currentRepeatCount < repeatCount) {
                    //可以重试
                    return true;
                }
                break;
            }
        }
        return false;
    }


}
