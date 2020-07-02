package com.github.chenlijia1111.utils.core.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 缓存工具类
 *
 * 适用场景：全局唯一，且有过期时间的数据，如微信公众号的 accessToken
 *
 * 未测试
 * @author Chen LiJia
 * @since 2020/7/2
 */
public class CacheUtil {


    private Map<String, CacheObject> cacheMap;

    private static volatile CacheUtil cacheUtil;

    /**
     * 私有构造函数
     */
    private CacheUtil() {
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
                    cacheUtil.cacheMap = new HashMap<>();
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


}
