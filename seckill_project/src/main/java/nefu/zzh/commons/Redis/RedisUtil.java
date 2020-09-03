package nefu.zzh.commons.Redis;

import com.alibaba.fastjson.JSON;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.util.ArrayList;
import java.util.List;

@Service
public class RedisUtil {

    /**
     * 获取对应key的value值
     * @param prefix
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    public <T> T get(KeyPrefix prefix, String key, Class<T> clazz){
        Jedis jedis = null;
        try{
            jedis = RedisPoolUtil.getPool().getResource();
            String realKey = prefix.getPrefix() + key;
            String str = jedis.get(realKey);
            T t = stringToBean(str, clazz);
            return t;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 设置对应key的value值
     * @param prefix
     * @param key
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean set(KeyPrefix prefix, String key, T value){
        Jedis jedis = null;
        try{
            jedis = RedisPoolUtil.getPool().getResource();
            String str = beanToString(value);
            if (str == null || str.length() <= 0){
                return false;
            }
            String realKey = prefix.getPrefix() + key;
            int second = prefix.expireSeconds();
            if (second <= 0){
                jedis.set(realKey, str);
            }else {
                jedis.setex(realKey, second, str);
            }
            jedis.set(realKey, str);
            return true;
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 判断key是否存在
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean exists(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = RedisPoolUtil.getPool().getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.exists(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 删除缓存，更新数据
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> boolean delete(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = RedisPoolUtil.getPool().getResource();
            //生成真正的key值
            String realKey = prefix.getPrefix() + key;
            long ret = jedis.del(realKey);
            return ret > 0;
        }finally {
            returnToPool(jedis);
        }
    }

    public boolean delete(KeyPrefix prefix){
        if (prefix == null){
            return false;
        }
        List<String> keys = scanKeys(prefix.getPrefix());
        if (keys == null || keys.size() <= 0){
            return true;
        }
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getPool().getResource();
            jedis.del(keys.toArray(new String [0]));
            return true;
        }catch (final Exception e){
            e.printStackTrace();
            return false;
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    private List<String> scanKeys(String prefix) {
        Jedis jedis = null;
        try {
            jedis = RedisPoolUtil.getPool().getResource();
            List<String> keys = new ArrayList<String>();
            String cursor = "0";
            ScanParams scanParams = new ScanParams();
            scanParams.match("*" + prefix + "*");
            scanParams.count(100);
            do{
                ScanResult<String> scanResult = jedis.scan(cursor, scanParams);
                List<String> result = scanResult.getResult();
                if (result != null && result.size() > 0){
                    keys.addAll(result);
                }
                cursor = scanResult.getCursor();
            }while (!cursor.equals("0"));
            return keys;
        }finally {
            if (jedis != null){
                jedis.close();
            }
        }
    }

    /**
     * 对应key的value值+1
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long incr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = RedisPoolUtil.getPool().getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.incr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    /**
     * 对应key的value值-1
     * @param prefix
     * @param key
     * @param <T>
     * @return
     */
    public <T> Long decr(KeyPrefix prefix, String key){
        Jedis jedis = null;
        try{
            jedis = RedisPoolUtil.getPool().getResource();
            String realKey = prefix.getPrefix() + key;
            return jedis.decr(realKey);
        }finally {
            returnToPool(jedis);
        }
    }

    public static  <T> String beanToString(T value) {
        if (value == null){
            return null;
        }
        Class<?> clazz = value.getClass();
        if (clazz == int.class || clazz ==Integer.class){
            return ""+value;
        }else if (clazz == long.class || clazz ==Long.class){
            return ""+value;
        }else if (clazz == String.class){
            return (String)value;
        }else {
            return JSON.toJSONString(value);
        }
    }

    public static  <T> T stringToBean(String str, Class clazz) {
        if (str == null || str.length() <= 0 || clazz == null){
            return null;
        }
        if (clazz == int.class || clazz ==Integer.class){
            return (T) Integer.valueOf(str);
        }else if (clazz == long.class || clazz ==Long.class){
            return (T) Long.valueOf(str);
        }else if (clazz == String.class){
            return (T) str;
        }else {
            return (T) JSON.toJavaObject(JSON.parseObject(str), clazz);
        }
    }

    private void returnToPool(Jedis jedis) {
        if (jedis != null){
            jedis.close();
        }
    }
}
