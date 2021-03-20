package com.sample.weeklyreportsummaryservice.service;

import com.sample.weeklyreportsummaryservice.config.RedisConfiguration;
import com.sample.weeklyreportsummaryservice.model.RedisKey;
import com.sample.weeklyreportsummaryservice.model.RedisValue;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Service
@ConditionalOnProperty(name="data.cache.service", havingValue="redis")
@Slf4j
public class RedisService implements DataCacheService {

    private final RedisConfiguration redisConfiguration;
    private final RedisTemplate<RedisKey,RedisValue> redisTemplate;

    public RedisService(RedisConfiguration redisConfiguration) {
        this.redisConfiguration = redisConfiguration;
        redisTemplate = redisConfiguration.redisTemplate();
        log.info("Redis cache is loaded.");
    }


    public boolean areKeysAvailable(final Set<RedisKey> redisKeys) {
        for (RedisKey redisKey : redisKeys) {
            if (redisTemplate.opsForValue().get(redisKey) == null) {
                return false;
            }
        }
        return true;
    }

    public Set<RedisValue> get(Set<RedisKey> redisKeys) {
        Set<RedisValue> redisValues = new HashSet<>();
        for (RedisKey redisKey : redisKeys) {
            redisValues.add(redisTemplate.opsForValue().get(redisKey));
        }
        return redisValues;
    }

    public RedisValue get(RedisKey redisKey) {
        return redisTemplate.opsForValue().get(redisKey);
    }

    public void put(Map<RedisKey, RedisValue> convertedDataSet) {
        for (Map.Entry<RedisKey, RedisValue> keyValues : convertedDataSet.entrySet()) {
            redisTemplate.opsForValue().set(keyValues.getKey(), keyValues.getValue());
        }
    }
}
