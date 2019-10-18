package com.eshanren.redis;

import com.jfinal.plugin.IPlugin;

/**
 * redis
 *
 * @author Ting on 2018-05-10.
 */
public class RedisPlugin implements IPlugin {

    @Override
    public boolean start() {
        try {
            RedisKit.getInstance();
        } catch (Exception e) {
//            LogKit.webLog.error("redis 启动失败;" + e.getMessage());
        }

        return true;
    }

    @Override
    public boolean stop() {
        try {
            RedisKit.getInstance().closeRedis();
        } catch (Exception e) {
//            LogKit.webLog.error("redis 关闭失败;" + e.getMessage());
        }

        return true;
    }
}
