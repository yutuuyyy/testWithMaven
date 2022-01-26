package com.test.mapper.redis;


import com.alibaba.fastjson.JSON;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class RedisMapper {

    //private redisTemplate redisTemplate;
    private RedisTemplate<String, Object> redisTemplate;

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    //@Resource
    //public void RedisMapper(redisTemplate redisTemplate) {
    //    this.redisTemplate = redisTemplate;
    //}

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String folder, String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(folder + key, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.expire(folder + key, 36000, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String folder, String key) {
        return redisTemplate.getExpire(folder + key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String folder, String key) {
        try {
            return redisTemplate.hasKey(folder + key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 查看哈希表 key 中，给定域 field 是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hexists(String folder, String key, String field) {
        try {
            return redisTemplate.opsForHash().hasKey(folder + key, field);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String folder, String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(folder + key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(folder + key));
            }
        }
    }


    public void delete(String folder) {
        redisTemplate.delete(folder);
    }

    /**
     * 功能描述: <br>
     * 〈获取以city为前缀key〉
     *
     * @Param:
     * @Return:
     * @Author: BaiYuTing
     * @Date:
     * @update: [自定义变更内容]
     */

    public Set<String> keys(String folder) {
        String pattern = folder + "*";
        Set<String> keys = redisTemplate.keys(pattern);
        return keys;
    }

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String folder, String key) {
        return key == null ? null : redisTemplate.opsForValue().get(folder + key);
    }

    /**
     * 功能描述: 普通缓存放入，全部对象都以JSON保存
     *
     * @Param: [folder, key, value]
     * @Return: boolean true成功 false失败
     * @Author: admin
     * @Date: 2021/7/19 11:03
     * @update: [序号] [时间] [修改人] [自定义变更内容]
     */
    public boolean set(String folder, String key, Object value) {
        try {
            String json = JSON.toJSONString(value);
            redisTemplate.opsForValue().set(folder + key, json);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String folder, String key, Object value, long time) {
        try {
            String json = JSON.toJSONString(value);
            if (time > 0) {
                redisTemplate.opsForValue().set(folder + key, json, time, TimeUnit.SECONDS);
            } else {
                redisTemplate.opsForValue().set(folder + key, json, 36000, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String folder, String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(folder + key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String folder, String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(folder + key, -delta);
    }

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值
     */
    public Object hget(String folder, String key, String item) {
        return redisTemplate.opsForHash().get(folder + key, item);
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String folder, String key) {
        return redisTemplate.opsForHash().entries(folder + key);
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String folder, String key, Map<String, Object> map) {
        try {
            redisTemplate.opsForHash().putAll(folder + key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String folder, String key, Map<String, Object> map, long time) {
        try {
            redisTemplate.opsForHash().putAll(folder + key, map);
            if (time > 0) {
                expire(folder, key, time);
            } else {
                expire(folder, key, 36000);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String folder, String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(folder + key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String folder, String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(folder + key, item, value);
            if (time > 0) {
                expire(folder, key, time);
            } else {
                expire(folder, key, 36000);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String folder, String key, Object... item) {
        redisTemplate.opsForHash().delete(folder + key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String folder, String key, String item) {
        return redisTemplate.opsForHash().hasKey(folder + key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String folder, String key, String item, double by) {
        return redisTemplate.opsForHash().increment(folder + key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String folder, String key, String item, double by) {
        return redisTemplate.opsForHash().increment(folder + key, item, -by);
    }

}