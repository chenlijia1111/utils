package com.github.chenlijia1111.utils.database.redis;

/**
 * redis请求接口
 *
 * @author Chen LiJia
 * @since 2020/3/16
 */
public interface IRedisConnect {

    /**
     * 存值
     *
     * @param key
     * @param value
     */
    void put(String key, Object value);

    /**
     * 存值并设置过期时间
     *
     * @param key
     * @param value
     * @param expireTime 毫秒
     */
    void putExpire(String key, Object value, Long expireTime);

    /**
     * 取值
     *
     * @param key
     * @return
     */
    String get(String key);

}
