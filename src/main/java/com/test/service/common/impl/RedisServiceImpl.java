package com.test.service.common.impl;

import com.test.mapper.redis.RedisMapper;
import com.test.service.common.RedisService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Map;
import java.util.Set;


@Service
public class RedisServiceImpl implements RedisService {

    @Resource
    private RedisMapper redisMapper;

    @Override
    public boolean expire(String folder, String key, long time) {
        folder = handleFolder(folder);
        return redisMapper.expire(folder, key, time);
    }

    @Override
    public long getExpire(String folder, String key) {
        folder = handleFolder(folder);
        return redisMapper.getExpire(folder, key);
    }

    @Override
    public boolean hasKey(String folder, String key) {
        folder = handleFolder(folder);
        return redisMapper.hasKey(folder, key);
    }

    @Override
    public void del(String folder, String... key) {
        folder = handleFolder(folder);
        redisMapper.del(folder, key);
    }
    @Override
    public void delete(String key) {
        redisMapper.delete(key);
    }

//    @Override
//    public Set<String> getAllKeys(String folder, String key){
//        return redisMapper.getAllKeys(folder,key);
//    }

    @Override
    public Set<String> keys(String folder){
        return redisMapper.keys(folder);
    }

    @Override
    public Object get(String folder, String key) {
        folder = handleFolder(folder);
        return redisMapper.get(folder, key);
    }

    @Override
    public boolean set(String folder, String key, Object value) {
        folder = handleFolder(folder);
        return redisMapper.set(folder, key, value);
    }

    @Override
    public boolean set(String folder, String key, Object value, long time) {
        folder = handleFolder(folder);
        return redisMapper.set(folder, key, value, time);
    }

    @Override
    public long incr(String folder, String key, long delta) {
        folder = handleFolder(folder);
        return redisMapper.incr(folder, key, delta);
    }

    @Override
    public long decr(String folder, String key, long delta) {
        folder = handleFolder(folder);
        return redisMapper.decr(folder, key, delta);
    }

    @Override
    public Object hget(String folder, String key, String item) {
        folder = handleFolder(folder);
        return redisMapper.hget(folder, key, item);
    }

    @Override
    public Map<Object, Object> hmget(String folder, String key) {
        folder = handleFolder(folder);
        return redisMapper.hmget(folder, key);
    }

    @Override
    public boolean hmset(String folder, String key, Map<String, Object> map) {
        folder = handleFolder(folder);
        return redisMapper.hmset(folder, key, map);
    }

    @Override
    public boolean hmset(String folder, String key, Map<String, Object> map, long time) {
        folder = handleFolder(folder);
        return redisMapper.hmset(folder, key, map, time);
    }

    @Override
    public boolean hset(String folder, String key, String item, Object value) {
        folder = handleFolder(folder);
        return redisMapper.hset(folder, key, item, value);
    }

    @Override
    public boolean hset(String folder, String key, String item, Object value, long time) {
        folder = handleFolder(folder);
        return redisMapper.hset(folder, key, item, value, time);
    }

    @Override
    public void hdel(String folder, String key, Object... item) {
        folder = handleFolder(folder);
        redisMapper.hdel(folder, key, item);
    }

    @Override
    public boolean hHasKey(String folder, String key, String item) {
        folder = handleFolder(folder);
        return redisMapper.hHasKey(folder, key, item);
    }

    @Override
    public double hincr(String folder, String key, String item, double by) {
        folder = handleFolder(folder);
        return redisMapper.hincr(folder, key, item, by);
    }

    @Override
    public double hdecr(String folder, String key, String item, double by) {
        folder = handleFolder(folder);
        return redisMapper.hdecr(folder, key, item, by);
    }

    @Override
    public boolean hexists(String folder, String key, String field) {
        folder = handleFolder(folder);
        return redisMapper.hexists(folder, key, field);
    }

    /**
     * 功能描述: <br>
     * 〈将文件夹后拼接上:〉
     *
     * @Param: [folder]
     * @Return: java.lang.String
     * @Author: fu
     * @Date: 2020/11/20 10:00
     * @update: [序号] [时间] [修改人] [自定义变更内容]
     */
    private String handleFolder(String folder) {
        if (null != folder && !folder.isEmpty()) {
            StringBuilder stringBuilder = new StringBuilder(folder);
            stringBuilder.append(":");
            folder = stringBuilder.toString();
        }
        return folder;
    }

    /**
     * 功能描述: <br>
     * 〈清除redis缓存〉
     * @Param:
     * @Return:
     * @Author: BaiYuTing
     * @Date:
     * @update: [自定义变更内容]
     */
    @Override
    public void removeRedisCache(String folder){
        Set<String> keys = keys(folder);
        for (String key : keys) {
            delete(key);
        }
    }

}