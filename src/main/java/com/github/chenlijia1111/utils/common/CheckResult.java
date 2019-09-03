package com.github.chenlijia1111.utils.common;

/**
 * 校验数据结果
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/9/3 0003 下午 1:21
 **/
public class CheckResult {

    /**
     * 校验结果
     *
     * @author chenlijia
     * @since 下午 1:22 2019/9/3 0003
     *
     **/
    private Boolean success;

    /**
     * 校验内容
     *
     * @author chenlijia
     * @since 下午 1:22 2019/9/3 0003
     **/
    private String msg;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    /**
     * 校验成功
     *
     * @param msg 1
     * @author chenlijia
     * @since 下午 1:24 2019/9/3 0003
     **/
    public static CheckResult success(String msg) {
        CheckResult checkResult = new CheckResult();
        checkResult.setSuccess(true);
        checkResult.setMsg(msg);
        return checkResult;
    }

    /**
     * 校验失败
     *
     * @param msg 1
     * @author chenlijia
     * @since 下午 1:24 2019/9/3 0003
     **/
    public static CheckResult failure(String msg) {
        CheckResult checkResult = new CheckResult();
        checkResult.setSuccess(false);
        checkResult.setMsg(msg);
        return checkResult;
    }
}
