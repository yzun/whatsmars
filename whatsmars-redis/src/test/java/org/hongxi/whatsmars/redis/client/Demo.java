package org.hongxi.whatsmars.redis.client;

import org.hongxi.whatsmars.redis.client.readwrite.ReadWriteRedisClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.*;

/**
 * Created by javahongxi on 2017/6/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-redis.xml")
public class Demo {

    @Autowired
    @Qualifier("singletonRedisClient")
    private JedisPool singletonRedisClient;

    @Autowired
    private ReadWriteRedisClient readWriteRedisClient;

//    @Autowired
    @Qualifier("redisClusterClient")
    private JedisCluster jedisCluster;

    @Autowired
    @Qualifier("shardedRedisClient")
    private ShardedJedisPool shardedRedisClient;

    @Test
    public void testSingleton() {
        Jedis jedis = singletonRedisClient.getResource();
        String cacheContent = null;
        try {
            cacheContent = jedis.get("hello_world");
        } finally {
            singletonRedisClient.close();
        }
        // 获取redis数据之后，立即释放连接，然后开始进行业务处理
        if(cacheContent == null) {
            // DB operation
        }
        // ..
    }

    @Test
    public void testReadWrite() {
        String cacheContent = null;
        try {
            readWriteRedisClient.set("hello_world", "Hi World!");
            cacheContent = readWriteRedisClient.get("hello_world");
            System.out.println(cacheContent);
        } catch (Exception e) {
            //如果异常，你可以决定是否忽略
        }
        if(cacheContent == null) {
            //如果cache中不存在，或者redis异常
        }
    }

    @Test
    public void testCluster() {
        String cacheContent = null;
        try {
            cacheContent = jedisCluster.get("hello_world");
        } catch (Exception e) {
            //如果异常，你可以决定是否忽略
        }
        if(cacheContent == null) {
            //如果cache中不存在，或者redis异常
        }
    }

    @Test
    public void testSharded() {
        ShardedJedis jedis = shardedRedisClient.getResource();
        String cacheContent = null;
        try {
            cacheContent = jedis.get("hello_world");
            System.out.println(cacheContent);
        } finally {
            shardedRedisClient.close();
        }
        // 获取redis数据之后，立即释放连接，然后开始进行业务处理
        if(cacheContent == null) {
            // DB operation
        }
        // ..
    }
}
