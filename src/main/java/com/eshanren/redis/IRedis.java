package com.eshanren.redis;

import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * Created by WWF
 */
public interface IRedis {

    /**
     * 获取redis中的数据
     *
     * @param key: redis key
     * @return cache object
     */
    public <T> T get(Object key);

    /**
     * 获取redis中的数据
     *
     * @param jedis:客户端
     * @param key:      redis key
     * @return cache object
     */
    public <T> T get(Jedis jedis, Object key);

    /**
     * 获取redis中的数据
     *
     * @param region: redis Region name
     * @param key:    redis key
     * @return cache object
     */
    public <T> T get(String region, Object key);

    /**
     * 获取redis中的数据
     *
     * @param jedis
     * @param region
     * @param key
     * @param <T>
     * @return
     */
    public <T> T get(Jedis jedis, String region, Object key);

    /**
     * 获取redis中的数据
     *
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getMulti(Object key);

    /**
     * 获取redis中的数据
     *
     * @param region
     * @param key
     * @param <T>
     * @return
     */
    public <T> T getMulti(String region, Object key);

    /**
     * 写入redis
     *
     * @param key:   redis key
     * @param value: redis value
     */
    public void set(Object key, Object value);

    /**
     * 写入redis
     *
     * @param region: redis Region name
     * @param key:    redis key
     * @param value:  redis value
     */
    public void set(String region, Object key, Object value);

    void set(Jedis jedis, Object key, Object value);

    void set(Jedis jedis, String region, Object key, Object value);

    /**
     * 批量写入
     *
     * @param key
     * @param value
     * @return
     */
    public RedisKit setMulti(Object key, Object value);

    /**
     * 批量写入
     *
     * @param region
     * @param key
     * @param value
     * @return
     */
    public RedisKit setMulti(String region, Object key, Object value);

    /**
     * 写入redis，有时效性
     *
     * @param key   redis key
     * @param value redis value
     * @param time  redis time
     */
    public void setx(Object key, Object value, int time);


    /**
     * 写入redis，有时效性
     *
     * @param region redis Region name
     * @param key    redis key
     * @param value  redis value
     * @param time   redis time
     */
    public void setx(String region, Object key, Object value, int time);


    /**
     * 将 key 中储存的数字值增一。
     *
     * @param key
     * @return
     */
    public Long incr(Object key);


    /**
     * 将 key 中储存的数字值增一。
     *
     * @param region
     * @param key
     * @return
     */
    public Long incr(String region, Object key);

    /**
     * 将 key 所储存的值加上增量 increment 。
     *
     * @param key
     * @param longValue
     * @return
     */
    public Long incrBy(Object key, long longValue);

    /**
     * 将 key 所储存的值加上增量 increment 。
     *
     * @param region
     * @param key
     * @param longValue
     * @return
     */
    public Long incrBy(String region, Object key, long longValue);

    /**
     * 将 key 中储存的数字值减一。
     *
     * @param key
     * @return
     */
    public Long decr(Object key);

    /**
     * 将rregion, key 中储存的数字值减一。
     *
     * @param region
     * @param key
     * @return
     */
    public Long decr(String region, Object key);

    /**
     * 将 key 所储存的值减去减量 decrement 。
     *
     * @param key
     * @param longValue
     * @return
     */
    public Long decrBy(Object key, long longValue);


    /**
     * 将 regison, key 所储存的值减去减量 decrement 。
     *
     * @param region
     * @param key
     * @param longValue
     * @return
     */
    public Long decrBy(String region, Object key, long longValue);


    /**
     * 删除redis
     *
     * @param key: redis key
     */
    public void evict(Object key);

    /**
     * 删除redis
     *
     * @param region: redis Region name
     * @param key:    redis key
     */
    public void evict(String region, Object key);

    /**
     * 批量删除redis
     *
     * @param keys: redis key
     */
    public void batchEvict(List keys);

    /**
     * 批量删除redis
     *
     * @param region: redis region name
     * @param keys:   redis key
     */
    public void batchEvict(String region, List keys);

    /**
     * Clear the redis
     *
     * @param region: redis region name
     */
    public void clear(String region);

    /**
     * Get redis region keys
     *
     * @param region: redis region name
     * @return key list
     */
    public List keys(String region);
}
