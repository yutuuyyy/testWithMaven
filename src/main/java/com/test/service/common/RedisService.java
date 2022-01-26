package com.test.service.common;

import java.util.Map;
import java.util.Set;


public interface RedisService {

    /**
     * 指定缓存失效时间
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键
     * @param time   时间(秒)
     * @return
     */
    public boolean expire(String folder, String key, long time);

    /**
     * 根据key 获取过期时间
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String folder, String key);

    /**
     * 判断key是否存在
     *
     * @param key    键
     * @param folder 文件夹 多层文件夹中间间隔:
     * @return true 存在 false不存在
     */
    public boolean hasKey(String folder, String key);


    /**
     * 查看哈希表 key 中，给定域 field 是否存在
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键
     * @return true 存在 false不存在
     */
    public boolean hexists(String folder, String key, String field);

    /**
     * 删除缓存
     *
     * @param key    可以传一个值 或多个
     * @param folder 文件夹 多层文件夹中间间隔:
     */
    public void del(String folder, String... key);

    public void delete(String key);

    public Set<String> keys(String folder);

    /**
     * 普通缓存获取
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键
     * @return 值
     */
    public Object get(String folder, String key);

    /**
     * 普通缓存放入
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键
     * @param value  值
     * @return true成功 false失败
     */
    public boolean set(String folder, String key, Object value);

    /**
     * 普通缓存放入并设置时间
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键
     * @param value  值
     * @param time   时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String folder, String key, Object value, long time);

    /**
     * 递增
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键
     * @param delta  要增加几(大于0)
     * @return
     */
    public long incr(String folder, String key, long delta);

    /**
     * 递减
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键
     * @param delta  要减少几(小于0)
     * @return
     */
    public long decr(String folder, String key, long delta);

    /**
     * HashGet
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键 不能为null
     * @param item   项 不能为null
     * @return 值
     */
    public Object hget(String folder, String key, String item);

    /**
     * 获取hashKey对应的所有键值
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hmget(String folder, String key);

    /**
     * HashSet
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键
     * @param map    对应多个键值
     * @return true 成功 false 失败
     */
    public boolean hmset(String folder, String key, Map<String, Object> map);

    /**
     * HashSet 并设置时间
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键
     * @param map    对应多个键值
     * @param time   时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String folder, String key, Map<String, Object> map, long time);

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键
     * @param item   项
     * @param value  值
     * @return true 成功 false失败
     */
    public boolean hset(String folder, String key, String item, Object value);

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键
     * @param item   项
     * @param value  值
     * @param time   时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String folder, String key, String item, Object value, long time);

    /**
     * 删除hash表中的值
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键 不能为null
     * @param item   项 可以使多个 不能为null
     */
    public void hdel(String folder, String key, Object... item);

    /**
     * 判断hash表中是否有该项的值
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键 不能为null
     * @param item   项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String folder, String key, String item);

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键
     * @param item   项
     * @param by     要增加几(大于0)
     * @return
     */
    public double hincr(String folder, String key, String item, double by);

    /**
     * hash递减
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @param key    键
     * @param item   项
     * @param by     要减少记(小于0)
     * @return
     */
    public double hdecr(String folder, String key, String item, double by);


    /**
     * 清除redis缓存 - 删除文件夹
     *
     * @param folder 文件夹 多层文件夹中间间隔:
     * @return
     */
    public void removeRedisCache(String folder);

}