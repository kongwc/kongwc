package com.kongwc.tools.common.database.accessor;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Nosql数据库访问器
 */
@Configuration
@Component
@Scope("prototype")
public class RedisAccessor {

    /**
     * CURSOR的开始和结束标识
     */
    private static final String CUSOR_START_END = "0";

    /**
     * DB 连接
     */
    @Value("${spring.datasource.redis.server}")
    private String server;

    /**
     * 端口
     */
    @Value("${spring.datasource.redis.port}")
    private int port;

    /**
     * 密码
     */
    @Value("${spring.datasource.redis.password:none}")
    private String password;

    /**
     * 连接池
     */
    private JedisPool jedisPool;

    /**
     * 初始化
     */
    @PostConstruct
    private void init() {

//        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
//        //Jedis Cluster will attempt to discover cluster nodes automatically
//        jedisClusterNodes.add(new HostAndPort(server, port));
//        jedisCluster = new JedisCluster(jedisClusterNodes, 0, 0, 0, password, null);

//        jedis = new Jedis(server, port);
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxTotal(maxTotal);
//        config.setMaxIdle(maxIdle);
//        config.setMaxWaitMillis(maxWaitMillis);
        if (StringUtils.isEmpty(password) || "none".equals(password)) {
            jedisPool = new JedisPool(new GenericObjectPoolConfig(), server, port,2000);
        } else {
            jedisPool = new JedisPool(new GenericObjectPoolConfig(), server, port,
                    2000, password, 0, null);
        }
    }

    /**
     * 设定值
     *
     * @param key   主键
     * @param value 值
     */
    public void set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.set(key, value);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    /**
     * 设定值
     *
     * @param key   主键
     * @param value 值
     */
    public void setex(String key, String value, int timeout) {
        Jedis jedis = jedisPool.getResource();
        try {
            jedis.setex(key, timeout, value);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    /**
     * 值获取
     *
     * @param key 主键
     * @return 值
     */
    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.get(key);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    /**
     * 判断是否存在
     *
     * @param key 主键
     * @return 是否存在
     */
    public boolean exists(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            return jedis.exists(key);
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

    /**
     * 根据主键查找所有数据
     *
     * @param key 主键
     * @return 数据List
     */
    public List<String> findAll(String key) {
        Jedis jedis = jedisPool.getResource();
        try {
            ScanParams params = new ScanParams();
            params.match(key).count(100);

            List<String> resultList = new ArrayList<>();
            String currentCursor = CUSOR_START_END;
            do {
                ScanResult<String> scanResult = jedis.scan(currentCursor, params);
                resultList.addAll(scanResult.getResult());
                currentCursor = scanResult.getStringCursor();
            } while (!CUSOR_START_END.equals(currentCursor));

            return resultList;
        } finally {
            //返还到连接池
            jedis.close();
        }
    }

}
