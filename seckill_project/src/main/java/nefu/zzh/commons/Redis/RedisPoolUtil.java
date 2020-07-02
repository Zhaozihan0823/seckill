package nefu.zzh.commons.Redis;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPoolUtil {
    private static volatile JedisPool pool;
    public static JedisPool getPool(){
        if (pool == null){
            synchronized (RedisPoolUtil.class){
                if (pool == null){
                    JedisPoolConfig config = new JedisPoolConfig();
                    config.setMaxTotal(10);
                    config.setMaxIdle(10);
                    config.setMaxWaitMillis(3);
                    pool = new JedisPool(config, "59.110.216.136", 6379, 3000, "wanan");
                }
            }
        }
        return pool;
    }
}
