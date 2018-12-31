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
        // 重命名
        jedis.rename("Brother Day", "Day");
        print(1, jedis.get("Day"));
        // 带过期时间
        // 缓存，验证码和短信验证码适用
        jedis.setex("hello2", 15, "good");

        // 数值操作
        jedis.set("pv", "100");
        // 加1
        jedis.incr("pv");
        print(2, jedis.get("pv"));
        // 加5
        jedis.incrBy("pv", 5);
        print(2, jedis.get("pv"));
        // 减2
        jedis.decrBy("pv", 2);
        print(2, jedis.get("pv"));

        // keys，取出所有keys
        print(3,jedis.keys("*"));

        // list
        String list = "list";
        // list插入
        for (int i = 0; i < 10; i++) {
            jedis.lpush(list, "a" + i);
        }
        // list取出jedis.lrange
        print(4, jedis.lrange("list", 0,10));
        print(4, jedis.lrange("list",0,4));

        // list 弹出一个值
        print(5, jedis.lpop("list"));
        // list长度
        print(6, jedis.llen("list"));
        // list指定某个index的元素
        print(7, jedis.lindex(list, 1));

        // list插入
        print(8, jedis.linsert("list", BinaryClient.LIST_POSITION.AFTER, "a5", "xx"));
        print(8, jedis.linsert("list", BinaryClient.LIST_POSITION.BEFORE, "a5", "xx"));
        print(8, jedis.lrange(list, 0, 10));

        // hash,业务扩展，添加额外属性，数据库操作不便的时候
        // 比如说数据库中有大多数字段不需要age,添加age字段，为了这一个人加一个age，浪费空间；不加吧，有用；
        // 另建一个表吧，链接查询，慢；那么就用redis的hash
        String user = "userKey";
        jedis.hset(user, "name", "Jim");
        jedis.hset(user, "age", "14");
        // 类似containKey
        print(9, jedis.hexists(user,"age"));
        print(10, jedis.hget(user, "name"));
        print(11, jedis.hgetAll(user));
        print(12, jedis.hincrBy(user, "age", 10));
        print(12, jedis.hgetAll(user));
        print(13, jedis.hdel(user,"name"));
        //遍历keys 和 vals
        print(14, jedis.hkeys(user));
        print(15, jedis.hvals(user));
        // nx not exist 如果不存在，就设置为bob
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
        // 取出集合的成员
        print(17, jedis.smembers(likeKey1));
        print(17, jedis.smembers(likeKey2));
        // 求异，1有2没有
        print(18, jedis.sdiff(likeKey1, likeKey2));
        print(18, jedis.sdiff(likeKey2, likeKey1));
        // 求交
        print(19, jedis.sinter(likeKey1, likeKey2));
        // 求并
        print(20, jedis.sunion(likeKey1, likeKey2));
        // 求有没有16这个对象
        print(21, jedis.sismember(likeKey1,"16"));
        // 删掉5
        jedis.srem(likeKey1, "5");
        print(22, jedis.smembers(likeKey1));
        // 把8从1移动到2
        jedis.smove(likeKey1, likeKey2, "8");
        print(23, jedis.smembers(likeKey1));
        print(23, jedis.smembers(likeKey2));
        // 计数likeKey1
        print(24, jedis.scard(likeKey1));
        // 随机取3个
        print(25, jedis.srandmember(likeKey1, 3));
        print(26, jedis.srandmember(likeKey1, 3));

        // sorted set;zset;优先级队列;堆
        String rank = "rank";
        jedis.zadd(rank, 90, "Bob");
        jedis.zadd(rank, 75, "Jim");
        jedis.zadd(rank, 60, "Lee");
        jedis.zadd(rank, 50, "John");
        // 计数
        print(27, jedis.zcard(rank));
        // zcount 统计区间内的数量
        print(28, jedis.zcount(rank, 60, 90));
        jedis.zincrby(rank, 3, "Bob:");
        // 查人的分数
        print(29, jedis.zscore(rank, "Bob"));
        // 默认从小到大排列
        print(30, jedis.zrange(rank, 0, 10));
        print(30, jedis.zrevrange(rank, 0, 2));
        // Tuple是Jedis里的
        for (Tuple tuple : jedis.zrangeByScoreWithScores(rank, 0, 100)) {
            print(31, tuple.getElement() + ":" + tuple.getScore());
        }
        // 查看Bob的排名
        print(31, jedis.zrank(rank, "Bob"));
        print(31, jedis.zrevrank(rank, "Bob"));
        // 根据字母排序
        String zset = "zset";
        jedis.zadd(zset, 1, "a");
        jedis.zadd(zset, 1, "b");
        jedis.zadd(zset, 1, "c");
        jedis.zadd(zset, 1, "d");
        // 边界[(
        print(32, jedis.zlexcount(zset, "(b", "[d"));
        print(32, jedis.zlexcount(zset, "[b", "[d"));
        // - 负无穷；+正无穷
        print(32, jedis.zlexcount(zset, "-", "+"));
        jedis.zrem(zset, "b");
        print(34, jedis.zrange(zset, 0, 10));
        // 根据字典序删除
        jedis.zremrangeByLex(zset, "(c", "+");
        print(35, jedis.zrange(zset, 0, 10));


        //issue 这块不懂:第7课，70min
//        JedisPool pool = new JedisPool();
//        for (int i = 0; i < 100; i++) {
//            Jedis j = pool.getResource();
//            System.out.println(j == jedis);
//            print(35, j.keys("*"));
//            print(35, j.get("pv"));
//            j.close();
//        }

        // 要点进去看JedisPool的构造方法，指明哪个数据库，否则就会像上面一样
        JedisPool pool = new JedisPool("redis://localhost:6379/9");
        for (int i = 0; i < 100; i++) {
            Jedis j = pool.getResource();
            print(36, j.get("pv"));
            // close()把这个资源还回去
            j.close();
        }


        // Redis做缓存
        User user1 = new User();
        user1.setPassword("1232");
        user1.setName("Johnson");
        user1.setHeadUrl("good.jgp");
        user1.setSalt("salt");
        // 序列化
        print(36, JSONObject.toJSONString(user1));
        // 解析，反序列化
        jedis.set("user1", JSONObject.toJSONString(user1));


        String value = jedis.get("user1");
        User user2 = JSONObject.parseObject(value, User.class);
        print(37, user2);


    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/11");

    }

    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e) {
            logger.error("jedis发生异常", e.getMessage());
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
            logger.error("jedis发生异常", e.getMessage());
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
            logger.error("jedis发生异常", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sIsMember(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, member);
        } catch (Exception e) {
            logger.error("jedis发生异常", e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }



}
