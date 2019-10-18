package com.eshanren.redis;

/**
 * Created by WWF
 */
public class RedisException extends RuntimeException {

    public RedisException(String s) {
        super(s);
    }

    public RedisException(String s, Throwable e) {
        super(s, e);
    }

    public RedisException(Throwable e) {
        super(e);
    }
}
