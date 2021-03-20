package com.sample.weeklyreportsummaryservice.service;

import com.sample.weeklyreportsummaryservice.model.RedisKey;
import com.sample.weeklyreportsummaryservice.model.RedisValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@ConditionalOnProperty(name="data.cache.service", havingValue="local")
@Slf4j
public class LocalCacheService implements DataCacheService {

    Map<RedisKey, RedisValue> data = new HashMap<>();

    public LocalCacheService() {
        log.info("Local cache service is loaded.");
    }

    public boolean areKeysAvailable(final Set<RedisKey> redisKeys) {
        if (redisKeys != null && data.keySet().containsAll(redisKeys)) {
            return true;
        } else {
            return false;
        }
    }

    public Set<RedisValue> get(Set<RedisKey> redisKeys) {
        Set<RedisValue> redisValues = new HashSet<>();
        for (RedisKey redisKey : redisKeys) {
            redisValues.add(data.get(redisKey));
        }
        return redisValues;
    }

    public RedisValue get(RedisKey redisKey) {
        return data.get(redisKey);
    }

    public void put(Map<RedisKey, RedisValue> convertedDataSet) {
        data.putAll(convertedDataSet);
    }
}
