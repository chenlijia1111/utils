package com.github.chenlijia1111.utils.core;

import com.github.chenlijia1111.utils.common.AssertUtil;

/**
 * 雪花算法 ID 生成
 * <p>
 * 总共64位
 * 1  位表示正负  这里固定取正 为0
 * 41 位表示当前时间戳与起始时间戳的差值
 * 5  位表示机器标识 初始化的时候固定
 * 5  位表示数据中心 初始化的时候固定
 * 12 位表示序列号  所以同一毫秒内，最多可以生成 1 << 12 即 4096 个id
 * <p>
 * 一天最多可以生成 4096 * 1000 * 60 * 60 * 24 个id
 * 如果觉得 一毫秒 4096 个id 无法满足系统 可以调节 序列号位 13 位
 *
 * @author 陈礼佳
 * @since 2019/9/9 22:12
 */
public class IDUtil {

    /**
     * 起始的时间戳
     */
    private final static long START_STAMP = 1480166465631L;

    /**
     * 每一部分占用的位数
     */
    private final static long SEQUENCE_BIT = 12; //序列号占用的位数
    private final static long MACHINE_BIT = 5;  //机器标识占用的位数
    private final static long DATA_CENTER_BIT = 5;//数据中心占用的位数

    /**
     * 每一部分的最大值
     */
    private final static long MAX_DATA_CENTER_NUM = -1L ^ (-1L << DATA_CENTER_BIT);
    private final static long MAX_MACHINE_NUM = -1L ^ (-1L << MACHINE_BIT);
    private final static long MAX_SEQUENCE = -1L ^ (-1L << SEQUENCE_BIT);

    /**
     * 每一部分向左的位移
     */
    private final static long MACHINE_LEFT = SEQUENCE_BIT;
    private final static long DATA_CENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT;
    private final static long TIMESTAMP_LEFT = DATA_CENTER_LEFT + DATA_CENTER_BIT;

    private long dataCenterId;  //数据中心
    private long machineId;    //机器标识
    private long sequence = 0L; //序列号
    private long lastStamp = -1L;//上一次时间戳

    /**
     * 构造方法
     *
     * @param dataCenterId 数据中心id
     * @param machineId    机器标识id
     */
    public IDUtil(long dataCenterId, long machineId) {

        //校验参数
        AssertUtil.isTrue(dataCenterId <= MAX_DATA_CENTER_NUM && dataCenterId >= 0,
                "数据中心的值不能大于最大数据中心" + MAX_DATA_CENTER_NUM + "也不能小于0");
        AssertUtil.isTrue(machineId <= MAX_MACHINE_NUM && machineId >= 0,
                "机器标识不能大于最大机器标识" + MAX_MACHINE_NUM + "也不能小于0");

        this.dataCenterId = dataCenterId;
        this.machineId = machineId;
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    public synchronized long nextId() {
        long currStamp = getNewStamp();
        if (currStamp < lastStamp) {
            throw new RuntimeException("Clock moved backwards.  Refusing to generate id");
        }

        if (currStamp == lastStamp) {
            //相同毫秒内，序列号自增
            sequence = (sequence + 1) & MAX_SEQUENCE;
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStamp = getNextMill();
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L;
        }

        lastStamp = currStamp;

        return (currStamp - START_STAMP) << TIMESTAMP_LEFT //时间戳部分
                | dataCenterId << DATA_CENTER_LEFT      //数据中心部分
                | machineId << MACHINE_LEFT            //机器标识部分
                | sequence;                            //序列号部分
    }

    private long getNextMill() {
        long mill = getNewStamp();
        while (mill <= lastStamp) {
            mill = getNewStamp();
        }
        return mill;
    }

    private long getNewStamp() {
        return System.currentTimeMillis();
    }
}
