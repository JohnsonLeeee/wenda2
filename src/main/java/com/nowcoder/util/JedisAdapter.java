package com.nowcoder.util;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.BinaryClient;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Tuple;

/**
 * @program: wenda
 * @description:
 * @author: Li Shuai
 * @create: 2018-11-29 15:26
 **/

// InitializingBean怎么用
@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool;

    public static void print(int index, Object obj) {
        System.out.println(String.format("%d, %s", index, obj.toString()));
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis://localhost:6379/9");
        // 删掉本数据库
        // jedis.flushAll()删掉所有数据库
        jedis.flushDB();
        jedis.set("Brother Day","good man");
        print(1, jedis.get("Brother Day"));
        jedis.rename("Brother Day", "Day");
        print(1, jedis.get("Day"));
        // 带过期时间
        // 缓存，验证码和短信验证码适用
        jedis.setex("hello2", 15, "good");

        // 数值操作
        jedis.set("pv", "100");
        jedis.incr("pv");
        print(2, jedis.get("pv"));
        jedis.incrBy("pv", 5);
        print(2, jedis.get("pv"));
        jedis.decrBy("pv", 2);
        print(2, jedis.get("pv"));

        // keys
        print(3,jedis.keys("*"));

        // list
        String list = "list";
        for (int i = 0; i < 10; i++) {
            jedis.lpush(list, "a" + i);
        }
        print(4, jedis.lrange("list", 0,10));
        print(4, jedis.lrange("list",0,4));
        print(5, jedis.lpop("list"));
        print(6, jedis.llen("list"));
        print(7, jedis.lindex(list, 1));
        print(8, jedis.linsert("list", BinaryClient.LIST_POSITION.AFTER, "a5", "xx"));
        print(8, jedis.linsert("list", BinaryClient.LIST_POSITION.BEFORE, "a5", "xx"));
        print(8, jedis.lrange(list, 0, 10));

        // hash
        String user = "userKey";
        jedis.hset(user, "name", "Jim");
        jedis.hset(user, "age", "14");
        print(9, jedis.hexists(user,"age"));
        print(10, jedis.hget(user, "name"));
        print(11, jedis.hgetAll(user));
        print(12, jedis.hincrBy(user, "age", 10));
        print(12, jedis.hgetAll(user));
        print(13, jedis.hdel(user,"name"));
        print(14, jedis.hkeys(user));
        print(15, jedis.hvals(user));
        jedis.hsetnx(user, "name", "Bob");
        jedis.hsetnx(user, "age", "20");
        print(16, jedis.hgetAll(user));

        //set
        String likeKey1 = "commentLike1";
        String likeKey2 = "commentLike2";
        for (int i = 0; i < 10; i++) {
            jedis.sadd(likeKey1, String.valueOf(i));
            jedis.sadd(likeKey2, String.valueOf(i*i));
        }
        print(17, jedis.smembers(likeKey1));
        print(17, jedis.smembers(likeKey2));
        print(18, jedis.sdiff(likeKey1, likeKey2));
        print(18, jedis.sdiff(likeKey2, likeKey1));
        print(19, jedis.sinter(likeKey1, likeKey2));
        print(20, jedis.sunion(likeKey1, likeKey2));
        print(21, jedis.sismember(likeKey1,"16"));
        jedis.srem(likeKey1, "5");
        print(22, jedis.smembers(likeKey1));
        jedis.smove(likeKey1, likeKey2, "8");
        print(23, jedis.smembers(likeKey1));
        print(23, jedis.smembers(likeKey2));
        print(24, jedis.scard(likeKey1));
        print(25, jedis.srandmember(likeKey1, 3));
        print(26, jedis.srandmember(likeKey1, 3));

        // sorted set;优先级队列，堆
        String rank = "rank";
        jedis.zadd(rank, 90, "Bob");
        jedis.zadd(rank, 75, "Jim");
        jedis.zadd(rank, 60, "Lee");
        jedis.zadd(rank, 50, "John");
        print(27, jedis.zcard(rank));
        print(28, jedis.zcount(rank, 60, 90));
        jedis.zincrby(rank, 3, "Bob:");
        print(29, jedis.zscore(rank, "Bob"));
        print(30, jedis.zrange(rank, 0, 10));
        print(30, jedis.zrevrange(rank, 0, 2));

        for (Tuple tuple : jedis.zrangeByScoreWithScores(rank, 0, 100)) {
            print(31, tuple.getElement() + ":" + tuple.getScore());
        }
        print(31, jedis.zrank(rank, "Bob"));
        print(31, jedis.zrevrank(rank, "Bob"));

        String zset = "zset";
        jedis.zadd(zset, 1, "a");
        jedis.zadd(zset, 1, "b");
        jedis.zadd(zset, 1, "c");
        jedis.zadd(zset, 1, "d");
        print(32, jedis.zlexcount(zset, "(b", "[d"));
        print(32, jedis.zlexcount(zset, "[b", "[d"));
        print(32, jedis.zlexcount(zset, "-", "+"));
        jedis.zrem(zset, "b");
        print(34, jedis.zrange(zset, 0, 10));


        //issue 这块不懂:第7课，70min
//        JedisPool pool = new JedisPool();
//        for (int i = 0; i < 100; i++) {
//            Jedis j = pool.getResource();
//            System.out.println(j == jedis);
//            print(35, j.keys("*"));
//            print(35, jedis.get("pv"));
//        }

        User user1 = new User();
        user1.setPassword("1232");
        user1.setName("Johnson");
        user1.setHeadUrl("good.jgp");
        user1.setSalt("salt");
        print(36, JSONObject.toJSONString(user1));
        jedis.set("user1", JSONObject.toJSONString(user1));


        String value = jedis.get("user1");
        User user2 = JSONObject.parseObject(value, User.class);
        int k =0;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");

    }

    public long sadd(String key, String... value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("发生异常", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key, String... value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("发生异常", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } catch (Exception e) {
            logger.error("发生异常", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, member);
        } catch (Exception e) {
            logger.error("发生异常", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }



}
