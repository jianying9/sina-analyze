
import com.wolf.sina.config.TableNames;
import java.util.Map;
import java.util.Set;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
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
    
    
    @Test
    public void testGet() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(10);
        poolConfig.setMinIdle(10);
        JedisPool jedisPool = new JedisPool(poolConfig, "192.168.235.234", 6379);
        Jedis jedis = jedisPool.getResource();
        String text = jedis.get("ssssssss");
        System.out.println(text);
        jedis.close();
    }
}