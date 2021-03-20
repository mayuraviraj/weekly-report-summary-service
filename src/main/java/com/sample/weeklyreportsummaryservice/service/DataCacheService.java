package com.sample.weeklyreportsummaryservice.service;

import com.sample.weeklyreportsummaryservice.model.RedisKey;
import com.sample.weeklyreportsummaryservice.model.RedisValue;

import java.util.Map;
import java.util.Set;

public interface DataCacheService {

    boolean areKeysAvailable(final Set<RedisKey> redisKeys);

    Set<RedisValue> get(Set<RedisKey> redisKeys);

    RedisValue get(RedisKey redisKey);

    void put(Map<RedisKey, RedisValue> data);
}
