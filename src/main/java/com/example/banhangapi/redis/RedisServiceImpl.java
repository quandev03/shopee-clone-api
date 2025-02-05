package com.example.banhangapi.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl {

    // Inject RedisTemplate via field-based injection
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void saveData(String key, Object value, long timeout) {
        // Check if redisTemplate is initialized
        if (redisTemplate != null) {
            redisTemplate.opsForValue().set(key, value, timeout, TimeUnit.MINUTES);
        } else {
            throw new IllegalStateException("RedisTemplate is not initialized!");
        }
    }

    public Object getData(String key) {
        if (redisTemplate != null) {
            return redisTemplate.opsForValue().get(key);
        } else {
            throw new IllegalStateException("RedisTemplate is not initialized!");
        }
    }

    public void removeData(String key) {
        if (redisTemplate != null) {
            redisTemplate.delete(key);
        } else {
            throw new IllegalStateException("RedisTemplate is not initialized!");
        }
    }

    public Boolean hasKey(String key) {
        if (redisTemplate != null) {
            return redisTemplate.hasKey(key);
        } else {
            throw new IllegalStateException("RedisTemplate is not initialized!");
        }
    }
//    public Object findById(String key, Long id){
//        Object listObject = getData(key);
//
//    }
}