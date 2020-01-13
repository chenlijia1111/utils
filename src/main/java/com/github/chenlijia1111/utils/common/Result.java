package com.github.chenlijia1111.utils.common;

import com.github.chenlijia1111.utils.common.enums.ResponseStatusEnum;
import com.github.chenlijia1111.utils.core.IOUtil;
import com.github.chenlijia1111.utils.core.StringUtils;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

/**
 * 返回结果
 * 新增国际化操作,可读取国际化properties文件 2020-01-13
 *
 * @author chenlijia
 * @version 1.0
 * @since 2019/3/8 0008 下午 5:53
 **/
public class Result {


    /**
     * 国际化解析文件得出的Map {@link #loadTransferPropertyRelativeFilePath(String)}
     * 也可以采用直接赋值方式赋值,不通过解析
     */
    public static Map transferPropertyKeyValueMap;


    /**
     * @author chenlijia
     * @Description 状态码
     * @Date 上午 10:29 2019/4/4 0004
     * @see ResponseStatusEnum
     **/
    private int code;

    private Boolean success;

    //返回信息
    private String msg;

    private Object data;

    public Result() {
    }

    public static Result success(String msg) {
        return success(msg, null);
    }

    public static Result success(String msg, Object data) {
        Result result = new Result();
        result.setCode(ResponseStatusEnum.SUCCESS.getCode());
        result.setSuccess(true);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    public static Result failure(String msg) {
        return failure(msg, null);
    }

    public static Result failure(String msg, Object data) {
        Result result = new Result();
        result.setCode(ResponseStatusEnum.FAIL.getCode());
        result.setSuccess(false);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }


    public static Result notLogin() {
        return notLogin("用户未登录");
    }


    public static Result notLogin(String msg) {
        Result result = new Result();
        result.setCode(ResponseStatusEnum.UN_LOGINED.getCode());
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }


    /**
     * 数据已存在
     *
     * @param msg  1
     * @param data 2
     * @return expertise.common.pojo.Result
     * @author chenlijia
     * @Description TODO
     * @Date 下午 3:19 2019/7/23 0023
     **/
    public static Result exists(String msg, Object data) {
        Result result = new Result();
        result.setCode(ResponseStatusEnum.EXISTS.getCode());
        result.setSuccess(false);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }

    /**
     * 数据不存在
     *
     * @param msg 1
     * @return expertise.common.pojo.Result
     * @author chenlijia
     * @Description TODO
     * @Date 下午 3:19 2019/7/23 0023
     **/
    public static Result notExists(String msg) {
        Result result = new Result();
        result.setCode(ResponseStatusEnum.NOT_EXISTS.getCode());
        result.setSuccess(false);
        result.setMsg(msg);
        return result;
    }


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

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
        //this.msg = msg;
        this.msg = checkMsg(msg);
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    /**
     * 读取国际化properties文件
     *
     * @param transferPropertyRelativeFilePath 相对项目位置,存放国际化 properties文件 如: static/test1.properties
     */
    public static void loadTransferPropertyRelativeFilePath(String transferPropertyRelativeFilePath) {
        //读取
        if (StringUtils.isNotEmpty(transferPropertyRelativeFilePath)) {
            InputStream inputStream = IOUtil.inputStreamToBaseProject(transferPropertyRelativeFilePath);
            if (Objects.nonNull(inputStream)) {
                Properties properties = IOUtil.readToProperties(inputStream);
                Result.transferPropertyKeyValueMap = properties;
            }
        }
    }

    @Override
    public String toString() {
        return "Result{" +
                "code=" + code +
                ", success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    /**
     * 处理返回内容,判断是否需要国际化
     *
     * @param str
     * @return
     */
    private String checkMsg(String str) {
        if (Objects.nonNull(transferPropertyKeyValueMap) && StringUtils.isNotEmpty(str)) {
            Object o = transferPropertyKeyValueMap.get(str);
            if (Objects.nonNull(o)) {
                return o.toString();
            }
        }
        return str;
    }

}
