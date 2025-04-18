package com.realtimechatapp.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPooled;

public class RedisConnection {
    private static final Logger LOG = LoggerFactory.getLogger(RedisConnection.class);


    public JedisPooled jedis;

    public RedisConnection(String host, int port) {

        this.jedis = new JedisPooled(host, port);

    }

    public JedisPooled getJedis() {
        return jedis;
    }

    public void closeJedis() {
        jedis.close();
        LOG.info("Closing Jedis");


    }
}
