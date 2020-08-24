package com.github.chenlijia1111.utils.core.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 缓存工具类
 *
 * 需要控制好 key 的名称，防止数据被覆盖
 *
 * 适用于所以需要按时间范围保存数据的场景，统一管理，节省资源
 *
 * @author Chen LiJia
 * @since 2020/7/2
 */
public class CacheUtil {

    //存储缓存数据的集合
    private Map<String, CacheObject> cacheMap;


    /**
     * 延时队列，用于清理数据
     */
    private DelayQueue<DelayCacheItem> delayCacheItemDelayQueue;

    //单例
    private static volatile CacheUtil cacheUtil;


    /**
     * 私有构造函数
     */
    private CacheUtil() {
        //初始化
        cacheMap = new HashMap<>();
        delayCacheItemDelayQueue = new DelayQueue<>();
        //清理缓存的线程
        new ClearCacheThread().start();

    }

    /**
     * 单例
     *
     * @return
     */
    public static CacheUtil getInstance() {
        if (null == cacheUtil) {
            synchronized (CacheUtil.class) {
                if (null == cacheUtil) {
                    cacheUtil = new CacheUtil();
                }
            }
        }
        return cacheUtil;
    }

    /**
     * 存入缓存对象
     *
     * @param cacheObject
     */
    public void put(CacheObject cacheObject) {
        if (Objects.nonNull(cacheObject)) {
            cacheMap.put(cacheObject.key, cacheObject);

            //存一份到队列里面去，定时删除
            DelayCacheItem delayCacheItem = new DelayCacheItem();
            delayCacheItem.cacheKey = cacheObject.key;
            delayCacheItem.limitTime = cacheObject.createTime + cacheObject.survivalTime;
            delayCacheItemDelayQueue.put(delayCacheItem);
        }
    }

    /**
     * 获取缓存对象
     *
     * @param key
     * @return
     */
    public CacheObject get(String key) {

        //判断是否存在
        CacheObject cacheObject = cacheMap.get(key);
        if (Objects.nonNull(cacheObject)) {
            //判断是否过期
            if (!cacheObject.isExpired()) {
                //没有过期
                return cacheObject;
            }

            //过期了，删除这个对象
            cacheMap.remove(key);
        }
        return null;
    }


    /**
     * 判断是否包含缓存
     *
     * @param key
     * @return
     */
    public boolean containsKey(String key) {
        //判断是否存在
        CacheObject cacheObject = cacheMap.get(key);
        if (Objects.nonNull(cacheObject)) {
            //判断是否过期
            if (!cacheObject.isExpired()) {
                //没有过期
                return true;
            }

            //过期了，删除这个对象
            cacheMap.remove(key);
        }

        return false;
    }

    /**
     * 删除缓存
     *
     * @param key
     * @return
     */
    public boolean remove(String key) {
        return Objects.nonNull(cacheMap.remove(key));
    }

    /**
     * 当前缓存数量
     * @return
     */
    public int currentCacheCount(){
        return cacheMap.size();
    }


    /**
     * 缓存延时队列对象
     * 每个缓存数据都会丢一份到延时队列里面去，到时间了就把他们删掉
     */
    private class DelayCacheItem implements Delayed {

        /**
         * key
         */
        private String cacheKey;


        /**
         * 存活到期时间
         * 计算方式：创建时间 + 存活时间
         */
        private long limitTime;


        @Override
        public long getDelay(TimeUnit unit) {
            return limitTime - System.currentTimeMillis();
        }

        @Override
        public int compareTo(Delayed o) {
            DelayCacheItem that = (DelayCacheItem) o;
            return Long.compare(this.limitTime, that.limitTime);
        }
    }

    /**
     * 清理缓存线程
     */
    private class ClearCacheThread extends Thread {

        @Override
        public void run() {
            while (true) {

                try {
                    DelayCacheItem cacheItem = delayCacheItemDelayQueue.take();
                    String cacheKey = cacheItem.cacheKey;

                    CacheObject cacheObject = cacheMap.get(cacheKey);
                    //判断是否存在
                    if (Objects.nonNull(cacheObject)) {
                        //判断是否失效
                        if (cacheObject.isExpired()) {
                            //失效了，删除数据
                            cacheMap.remove(cacheKey);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }


}
