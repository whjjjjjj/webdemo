package com.eshanren.redis;

import com.eshanren.utils.StringUtil;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import net.oschina.j2cache.util.SerializationUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * redis操作类
 *
 */
public class RedisKit implements IRedis {

    /**
     * redis 池
     */
    private static JedisPool jedisPool;

    /**
     * rediskit 单例
     */
    private static RedisKit instance;

    /**
     * 批量设置时临时保存的jedis
     */
    private Jedis jedis = null;

    /**
     * 序列化方式
     */
    private static String serialization;

    public RedisKit(){
        Prop prop = PropKit.use("redis.properties","utf-8");

        String host = prop.get("redis.host","127.0.0.1");
        int port = prop.getInt("redis.port",6379);
        int timeout = prop.getInt("redis.timeout",2000);
        String password = prop.get("redis.password","");
        String serialization = prop.get("redis.serialization","fst");
        if (StringUtil.isEmpty(password)) {
            password = null;
        }
        int database = prop.getInt("redis.database",2);
        init(host,port,timeout,password,serialization,database);

    }

    /**
     * 初始化
     * @param host
     * @param port
     * @param timeout
     * @param password
     * @param serialization
     * @param database
     */
    public void init(String host, int port, int timeout, String password, String serialization, int database) {
        try {
            RedisKit.serialization = serialization;

            JedisPoolConfig config = new JedisPoolConfig();
            //最大数量
            config.setMaxTotal(300);
            //最小空闲数
            config.setMinIdle(50);
            //5s超时
            config.setMaxWaitMillis(5000);
            config.setMinEvictableIdleTimeMillis(5000);
            config.setTimeBetweenEvictionRunsMillis(5000);

            jedisPool = new JedisPool(config, host, port, timeout, password, database);
        } catch (Exception e) {
            throw new RedisException("Redis Init Fail:" + e.getMessage());
        }
    }

    public static String getSerialization() {
        return serialization;
    }

    /**
     * 获取单例
     * @return
     */
    public static RedisKit getInstance(){
        if (instance == null) {
            instance = new RedisKit();
        }
        return instance;
    }

    /**
     * 关闭Redis
     */
    public void closeRedis() {
        try {
            jedisPool.close();
            jedisPool = null;
        } catch (Exception e) {
            throw new RedisException("Redis Close Fail :" + e.getMessage());
        }
    }

    /**
     * 关闭Jedis
     */
    public void closeJedis(){
        try {
            if (jedis != null) {
                jedis.close();
                jedis = null;
            }
        } catch (Exception e) {
            throw new RedisException("Jedis Close Fail :" + e.getMessage());
        }
    }

    /**
     * 关闭Jedis
     * @param jedis
     */
    public void closeJedis(Jedis jedis) {
        try {
            if (jedis != null) {
                jedis.close();
            }
        } catch (Exception e) {
            throw new RedisException("Jedis Close Fail :" + e.getMessage());
        }
    }

    /**
     * 获取资源连接
     * @return
     */
    public Jedis getResource(){
        Jedis jedis = jedisPool.getResource();
        if (jedis == null) {
            throw new RedisException("jedis is null");
        }
        return jedis;
    }

    @Override
    public <T> T get(Object key) {
        return get("",key);
    }

    @Override
    public <T> T get(Jedis jedis, Object key) {
        return get(jedis,"",key);
    }

    @Override
    public <T> T get(String region, Object key) {
        if (key == null) {
            return null;
        }

        Jedis jedis = null;
        try {
            jedis = getResource();
            return get(jedis,region,key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public <T> T get(Jedis jedis, String region, Object key) {
        if (jedis == null) {
            return null;
        }

        Object obj = null;
        try {
            byte[] bytes = jedis.get(getKeyName(region,key).getBytes());
            obj = deserialize(bytes);
            return (T) obj;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    /**
     * 批量获取数据
     * @param key
     * @param <T>
     * @return
     */
    @Override
    public <T> T getMulti(Object key) {
        return getMulti("",key);
    }

    @Override
    public <T> T getMulti(String region, Object key) {
        try {
            if (jedis == null) {
                System.out.println("获取jedis");
            }
            jedis = jedis == null ? getResource() : jedis;
            return get(jedis,region,key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void set(Object key, Object value) {
        set("",key,value);
    }

    @Override
    public void set(String region, Object key, Object value) {
        if (key == null || value == null) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.set(getKeyName(region,key).getBytes(),SerializationUtils.serialize(value));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public void set(Jedis jedis, Object key, Object value) {
        set(jedis,"",key,value);
    }

    @Override
    public void set(Jedis jedis, String region, Object key, Object value) {
        try {
            jedis = jedis == null ? getResource() : jedis;
            jedis.set(getKeyName(region,key).getBytes(),SerializationUtils.serialize(value));
        } catch (Exception e ) {
            e.printStackTrace();
        }
    }

    /**
     * 批量设置,设置完成后需要调用closeJedis进行关闭
     * @param key
     * @param value
     * @return
     */
    @Override
    public RedisKit setMulti(Object key, Object value) {
        return setMulti("",key,value);
    }

    @Override
    public RedisKit setMulti(String region, Object key, Object value) {
        if (key == null | value == null) {
            return this;
        }

        try {
            jedis = jedis == null ? getResource() : jedis;
            jedis.set(getKeyName(region,key).getBytes(),SerializationUtils.serialize(value));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    @Override
    public void setx(Object key, Object value, int time) {
        setx("",key,time);
    }

    @Override
    public void setx(String region, Object key, Object value, int time) {
        if (key == null || value == null) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.setex(getKeyName(region,key).getBytes(),time,SerializationUtils.serialize(value));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public Long incr(Object key) {
        return incr("",key);
    }

    @Override
    public Long incr(String region, Object key) {
        if (key == null) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.incr(getKeyName(region,key).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public Long incrBy(Object key, long longValue) {
        return incrBy("",key,longValue);
    }

    @Override
    public Long incrBy(String region, Object key, long longValue) {
        if (key == null) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = getResource();
            Long value = jedis.incrBy(getKeyName(region, key).getBytes(),longValue);
            return value;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public Long decr(Object key) {
        return decr("",key);
    }

    @Override
    public Long decr(String region, Object key) {
        if (key == null) {
            return null;
        }
        Jedis jedis = null;
        try {
            jedis = getResource();
            return jedis.decr(getKeyName(region, key).getBytes());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public Long decrBy(Object key, long longValue) {
        return decrBy("",key,longValue);
    }

    @Override
    public Long decrBy(String region, Object key, long longValue) {
        if (key == null) {
            return null;
        }
        Jedis jedis = null;
       try {
           jedis = getResource();
           Long value = jedis.incrBy(getKeyName(region, key).getBytes(),longValue);
           return value;
       } catch (Exception e) {
           e.printStackTrace();
           return null;
       } finally {
           closeJedis(jedis);
       }
    }

    @Override
    public void evict(Object key) {
        evict("",key);
    }

    @Override
    public void evict(String region, Object key) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            jedis.del(getKeyName(region, key));
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public void batchEvict(List keys) {
        batchEvict("",keys);
    }

    @Override
    public void batchEvict(String region, List keys) {
        if (keys == null || keys.size() == 0) {
            return;
        }
        Jedis jedis = null;
        try {
            jedis = getResource();
            String[] okeys = new String[keys.size()];
            for (int i = 0 ; i < okeys.length ; i++) {
                okeys[i] = getKeyName(region,keys.get(i));
            }
            jedis.del(okeys);
        } catch (Exception e) {
            throw new RedisException(e);
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public void clear(String region) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            String[] keys = new String[]{};
            keys = jedis.keys(region + ":*").toArray(keys);
            jedis.del(keys);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeJedis(jedis);
        }
    }

    @Override
    public List keys(String region) {
        Jedis jedis = null;
        try {
            jedis = getResource();
            List<String> keys = new ArrayList<String>();
            keys.addAll(jedis.keys(region + ":*"));
            for (int i = 0; i < keys.size(); i++) {
                keys.set(i, keys.get(i).substring(region.length() + 3));
            }
            return keys;
        } catch (Exception e) {
            return null;
        } finally {
            closeJedis(jedis);
        }
    }

    /**
     * 生成key
     * @param region
     * @param key
     * @return
     */
    public static String getKeyName(String region, Object key) {
        if (key instanceof Number) {
            return region + ":I:" + key;
        } else {
            Class keyClass = key.getClass();
            if (String.class.equals(keyClass) || StringBuilder.class.equals(keyClass) || StringBuffer.class.equals(keyClass)) {
                return region + ":S:" + key;
            }
        }
        return region + ":O:" +key;
    }

    /**
     * 反序列化
     * @param bytes
     * @return
     * @throws IOException
     */
    public static Object deserialize(byte[] bytes) throws IOException {
        if (bytes != null) {
            try {
                return SerializationUtils.deserialize(bytes);
            } catch (Exception e) {

            }
        }
        return null;
    }
}
