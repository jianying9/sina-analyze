package com.wolf.sina;


import com.wolf.sina.config.TableNames;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.io.FileUtils;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 *
 * @author jianying9
 */
public class UpdateJUnitTest {

    public UpdateJUnitTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }
    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //

//    @Test
    public void moveDb() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(10);
        JedisPool jedisPool = new JedisPool(poolConfig, "192.168.235.234", 6379);
        String keyIndex = "KEY_" + TableNames.S_TAG_CUBE;
        String keyPrefix = TableNames.S_TAG_CUBE + "_";
        int dbIndex = TableNames.S_TAG_CUBE_INDEX;
        Jedis jedis = jedisPool.getResource();
        jedis.select(0);
        long total = jedis.zcard(keyIndex);
        long start = 0;
        long end = 0;
        long pageSize = 500;
        Set<String> keySet;
        Map<String, String> entityMap;
        String rowKey;
        while (start < total) {
            System.out.println(start);
            end = start + pageSize - 1;
            if (end > total) {
                end = total - 1;
            }
            //读取
            jedis.select(0);
            keySet = jedis.zrevrange(keyIndex, start, end);
            for (String key : keySet) {
                rowKey = keyPrefix + key;
                jedis.select(0);
                entityMap = jedis.hgetAll(rowKey);
                if (entityMap.isEmpty()) {
//                    jedis.del(rowKey);
                    jedis.zrem(keyIndex, key);
                } else {
//                    jedis.del(rowKey);
                    //
                    jedis.select(dbIndex);
                    jedis.hmset(key, entityMap);
                }
            }
            start = start + pageSize;
        }
        jedis.close();
    }

//    @Test
    public void delete() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(10);
        JedisPool jedisPool = new JedisPool(poolConfig, "192.168.235.234", 6379);
        Jedis jedis = jedisPool.getResource();
        jedis.select(0);
        Set<String> keySet = jedis.keys("SINA_USER_CUBE_*");
        for (String key : keySet) {
            jedis.del(key);
        }
        jedis.close();
    }

//    @Test
    public void rebuidKey() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(10);
        JedisPool jedisPool = new JedisPool(poolConfig, "192.168.235.234", 6379);
        String keyIndex = "KEY_" + TableNames.S_TAG_CUBE;
        Jedis jedis = jedisPool.getResource();
        jedis.select(TableNames.S_TAG_CUBE_INDEX);
        Set<String> keySet = jedis.keys("*");
        for (String key : keySet) {
            System.out.println(key);
            jedis.select(TableNames.S_TAG_CUBE_INDEX);
            String num = jedis.hget(key, "num");
            jedis.select(0);
            jedis.zadd(keyIndex, Long.parseLong(num), key);
        }
        jedis.close();
    }

//    @Test
    public void testSelect() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(10);
        JedisPool jedisPool = new JedisPool(poolConfig, "192.168.235.234", 6379);
        Jedis jedis = jedisPool.getResource();
        for (int index = 0; index < 20; index++) {
            String text = jedis.select(index);
            System.out.println(index + ":" + text);
        }
        jedis.close();
    }

//    @Test
    public void testGet() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(10);
        JedisPool jedisPool = new JedisPool(poolConfig, "192.168.235.234", 6379);
        Jedis jedis = jedisPool.getResource();
        String text = jedis.get("ssssssss");
        Double d = jedis.zscore("KEY_S_SINA_USER", "1");
        jedis.select(TableNames.S_SINA_USER_INDEX);
        Map<String, String> map = jedis.hgetAll("1");
        System.out.println(text);
        jedis.close();
    }

    private List<String> readFromFile(String userId) {
        List<String> resultList = new ArrayList<String>(0);
        String sinaUserInfoFilePath = "/data/file/sina-user-info/";
        String userFilePath = sinaUserInfoFilePath.concat(userId);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = null;
        try {
            String json = FileUtils.readFileToString(new File(userFilePath));
            rootNode = mapper.readValue(json, JsonNode.class);
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        if (rootNode != null) {
            //读数据
            JsonNode followNode = rootNode.get("follow");
            if(followNode != null) {
                String follows = followNode.getTextValue();
                String[] followArray = follows.split(",");
                resultList = Arrays.asList(followArray);
            }
        }
        return resultList;
    }

//    @Test
    public void testImport() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(10);
        JedisPool jedisPool = new JedisPool(poolConfig, "192.168.235.234", 6379);
        Jedis jedis = jedisPool.getResource();
        long pageIndex = 1;
        long pageSize = 100;
        while (pageIndex < 380) {
            System.out.println(pageIndex);
            long start = (pageIndex - 1) * pageSize;
            long end = pageIndex * pageSize - 1;
            Set<String> keyValueSet = jedis.zrevrange("KEY_S_SINA_USER", start, end);
            for (String keyValue : keyValueSet) {
                List<String> userIdList = this.readFromFile(keyValue);
                for (String userId : userIdList) {
                    Double score = jedis.zscore("KEY_S_SINA_USER", userId);
                    if(score == null) {
                        jedis.zadd("KEY_S_SINA_USER", -1, userId);
                    }
                }
            }
            pageIndex++;
        }
        jedis.close();
    }
}