package com.example.banhangapi.redis;

public interface RedisService {
    void saveData(String key, Object value, long timeout);
    Object getData(String key);
    void removeData(String key);
    Boolean hasKey(String key);
}