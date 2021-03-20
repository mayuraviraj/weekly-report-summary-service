package com.sample.weeklyreportsummaryservice.service;

import com.sample.weeklyreportsummaryservice.model.RedisKey;
import com.sample.weeklyreportsummaryservice.model.RedisValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class RedisServiceTest {

    private DataCacheService dataCacheService;

    @BeforeEach
    public void beforeEach(){
        dataCacheService = new LocalCacheService();
    }

    @Test
    public void testGetDataWithNullData() {
        try {
            RedisValue redisValue = dataCacheService.get(new RedisKey(null, null, null));
            assertNull(redisValue);
        } catch (Exception e) {
            fail("Exception not expected for get operation with false key.");
        }
    }

    @Test
    public void testAreAllKeysAvailableWithNullKey() {
        try {
            assertFalse(dataCacheService.areKeysAvailable(null));
        } catch (Exception e) {
            e.printStackTrace();
            fail("Exception not expected for get operation with false key.");
        }
    }

    @Test
    public void testAreAllKeysAvailableWithExistingKey() {
        try {
            Map<RedisKey, RedisValue> data = new HashMap<>();
            RedisKey key = new RedisKey("sample_project", "bug", "open");
            data.put(key, new RedisValue("sample_issue", Collections.EMPTY_LIST));
            dataCacheService.put(data);
            Set<RedisKey> keys = new HashSet<>();
            keys.add(key);
            assertTrue(dataCacheService.areKeysAvailable(keys));
        } catch (Exception e) {
            fail("Exception not expected for get operation with false key.");
        }
    }
}