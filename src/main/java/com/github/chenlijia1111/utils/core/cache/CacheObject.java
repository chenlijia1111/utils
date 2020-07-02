package com.github.chenlijia1111.utils.core.cache;

import com.github.chenlijia1111.utils.common.AssertUtil;

import java.util.Objects;

/**
 * 缓存对象
 *
 * @author Chen LiJia
 * @since 2020/7/2
 */
public class CacheObject {

    /**
     * key
     */
    protected final String key;

    /**
     * 缓存值
     * 本来决定用泛型的，但是用了泛型之后，
     * 每种缓存数据都得创建一个缓存工具类
     * 于是还是使用 Object 来接收数据，返回值自己转换即可
     */
    protected final Object obj;

    /**
     * 超时类型
     */
    private TimeOutTypeEnum timeOutTypeEnum;

    /**
     * 上次访问时间
     * 初始值为创建时间
     * 以后每请求一次，更新时间
     */
    private long lastAccessTime = System.currentTimeMillis();

    /**
     * 存活时间
     * 单位 毫秒
     */
    private final long survivalTime;

    /**
     * 数据创建时间
     */
    private final long createTime = System.currentTimeMillis();

    /**
     * 构造方法
     *
     * @param key
     * @param obj
     * @param survivalTime
     * @param timeOutTypeEnum
     */
    public CacheObject(String key, Object obj, long survivalTime, TimeOutTypeEnum timeOutTypeEnum) {

        //判断参数
        AssertUtil.notNull(key, "缓存key不能为空");
        AssertUtil.notNull(obj, "缓存值不能为空");
        AssertUtil.notNull(survivalTime, "缓存存活时间不能为空");
        AssertUtil.notNull(timeOutTypeEnum, "缓存存活时间类型不能为空");

        this.key = key;
        this.obj = obj;
        this.survivalTime = survivalTime;
        this.timeOutTypeEnum = timeOutTypeEnum;
    }

    /**
     * 判断数据是否过期
     *
     * @return
     */
    boolean isExpired() {

        if (Objects.equals(TimeOutTypeEnum.NOT_TIME_OUT, timeOutTypeEnum)) {
            //永不超时
            return false;
        }

        if (Objects.equals(TimeOutTypeEnum.FIXED_TIME_OUT, timeOutTypeEnum)) {
            //固定时间超时
            if (System.currentTimeMillis() < createTime + survivalTime) {
                return false;
            }
        }

        if (Objects.equals(TimeOutTypeEnum.DELAY_TIME_OUT, timeOutTypeEnum)) {
            //延时超时类型
            //判断上次访问时间
            if (System.currentTimeMillis() < lastAccessTime + survivalTime) {
                return false;
            }
        }

        //超时了

        return true;
    }


    /**
     * 获取key
     *
     * @return
     */
    public String getKey() {
        return key;
    }

    /**
     * 获取值
     *
     * @return
     */
    public Object getValue() {
        //更新上次请求的时间
        lastAccessTime = System.currentTimeMillis();
        return obj;
    }

    @Override
    public String toString() {
        return "CacheObject{" +
                "key='" + key + '\'' +
                ", obj=" + obj +
                ", timeOutTypeEnum=" + timeOutTypeEnum +
                ", lastAccessTime=" + lastAccessTime +
                ", survivalTime=" + survivalTime +
                ", createTime=" + createTime +
                '}';
    }
}
