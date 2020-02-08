package com.video.download.common.redis;

import com.alibaba.fastjson.JSON;
import com.video.download.common.context.SpringContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.util.Assert;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.commands.JedisCommands;

import java.util.Collections;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 * @Author Xiaoming
 * @Date 2019/09/11 14:46
 * @Version 1.0.0
 **/
@Slf4j
public class RedisUtil {

    //Singleton
    public volatile static RedisUtil _REDIS_MODULE;
    private StringRedisTemplate stringRedisTemplate;
    //Redis connection factory
    private JedisConnectionFactory connectionFactory;
    //Default database index is zero
    private static int _DEFAULT_DATABASE_INDEX = 0;

    private static final String LOCK_SUCCESS = "OK";
    private static final String SET_IF_NOT_EXIST = "NX";
    private static final String SET_WITH_EXPIRE_TIME = "EX";
    private static final Long RELEASE_SUCCESS = 1L;

    /**
     * Init redis utils client
     * use singleton model
     */
    public synchronized static void initRedis(){
        if(_REDIS_MODULE == null){
            synchronized (RedisUtil.class){
                _REDIS_MODULE = new RedisUtil();
            }
        }else{
            log.info("【Redis utils】Global redis install already exist. Don't repeat process init function");
        }
    }

    /**
     * Get redis template and basis connection from the spring context
     */
    private RedisUtil() {
        this.stringRedisTemplate = SpringContext.getBean(StringRedisTemplate.class);
        this.connectionFactory = SpringContext.getBean(JedisConnectionFactory.class);
    }

    public StringRedisTemplate getStringRedisTemplate() {
        return stringRedisTemplate;
    }

    /**
     * Get redis utils example
     */
    public static RedisUtil getInstance(){
        return _REDIS_MODULE;
    }

    /**
     * @param key            Cache key
     * @param value          Cache value
     * @param expire         Cache expire time unit's seconds
     * @param database       database index
     */
    public void set(String key, String value, Long expire, Integer database){
        Assert.isTrue(!(key == null),"Key cannot be empty when setting the cache!!!");
        selectedDatabase(database);
        if(expire == null){
            //Insert key-value to redis according to the no expire time
            stringRedisTemplate.opsForValue().set(key,value);
        }else{
            if(value == null){
                //Delay key expire time
                stringRedisTemplate.expire(key,expire, TimeUnit.SECONDS);
            }else{
                //Insert key-value to redis
                stringRedisTemplate.opsForValue().set(key,value,expire, TimeUnit.SECONDS);
            }
        }
    }
    /**
     * Get the cache from the specified library
     */
    public String get(String key, Integer database){
        Assert.notNull(key,"When getting the cache from redis, key must not be empty!!!");
        selectedDatabase(database);
        return stringRedisTemplate.opsForValue().get(key);
    }

    /**
     * Cache key-value to redis set queue
     */
    public void sadd(String key, Object value, Integer database){
        Assert.notNull(key,"When sadd the cache from redis, key must not be empty!!!");
        selectedDatabase(database);
        if(value instanceof String){
            stringRedisTemplate.opsForSet().add(key, value.toString());
        }else{
            stringRedisTemplate.opsForSet().add(key, Optional.ofNullable(value)
                    .map(JSON::toJSONString).orElse(""));
        }
    }

    /**
     * Random get a options with redis set queue
     */
    public String spop(String key){
        Assert.notNull(key,"spop value to the cache from redis, key must not be empty!!!");
        return stringRedisTemplate.opsForSet().pop(key);
    }

    /**
     * Get the cache and trans example model
     */
    public <V> V getOfModel(String key, Class<V> cls){
        String val = this.get(key);
        if(val == null){
            return null;
        }
        return JSON.parseObject(val,cls);
    }

    public <V> V getOfModel(String key, Integer dataBase, Class<V> cls){
        String val = this.get(key,dataBase);
        if(val == null){
            return null;
        }
        return JSON.parseObject(val,cls);
    }

    public void del(String key, Integer database){
        Assert.isTrue(!(key == null),"Key cannot be empty when delete cache!!!");
        selectedDatabase(database);
        stringRedisTemplate.delete(key);
    }

    public void del(String key){
        this.del(key,null);
    }

    /**
     * Switch redis database and selected
     */
    private void selectedDatabase(Integer database){
        database = (database == null ? _DEFAULT_DATABASE_INDEX : database);
        //Selected cache database index
        connectionFactory.setDatabase(database);
    }

    /**
     * Add cache and set expire
     */
    public void set(String key, String value, Long expire){
        this.set(key, value, expire,null);
    }
    /**
     * Add cache and selected specified database
     */
    public void set(String key, String value, Integer dataBase){
        this.set(key, value, null,dataBase);
    }
    /**
     * Add cache and use default config
     */
    public void set(String key, String value){
        this.set(key, value,null,null);
    }
    /**
     * Add object cache and set expireTime
     */
    public void set(String key, Object value, Long expire){
        this.set(key, JSON.toJSONString(value),expire,null);
    }
    /**
     * Add object cache to specified database
     */
    public void set(String key, Object value, Integer dataBase){
        this.set(key, JSON.toJSONString(value),null,dataBase);
    }
    /**
     * Add object cache and set expireTime,dataBase
     */
    public void set(String key, Object value, Long expire, Integer dataBase){
        this.set(key,JSON.toJSONString(value),expire,dataBase);
    }
    /**
     * Delay key expire time
     */
    public void set(String key, Long expire){
        this.set(key,null,expire,null);
    }
    /**
     * Add object cache and use default config
     */
    public void set(String key, Object value){
        this.set(key, JSON.toJSONString(value),null,null);
    }

    /**
     * Get value form redis cache,Default use one database
     */
    public String get(String key){
        return get(key,null);
    }

    public void sadd(String key, Object value){
        this.sadd(key, value,null);
    }

    public void sadd(String key, String value){
        this.sadd(key,value,null);
    }

    /**
     * Whether the value exists in the key's cache?
     * @return bool
     */
    public boolean isMember(String key, String val){
        return stringRedisTemplate.opsForSet().isMember(key,val);
    }


    /**
     * 释放锁
     */
    public boolean unLock(String lockKey, String lockVal) {
        String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
        Long result = stringRedisTemplate.execute((RedisCallback<Long>) connection -> {
            Object nativeConnection = connection.getNativeConnection();
            return (Long)((Jedis) nativeConnection).eval(script, Collections.singletonList(lockKey), Collections.singletonList(lockVal));
        });
        return RELEASE_SUCCESS.equals(result);
    }
}
