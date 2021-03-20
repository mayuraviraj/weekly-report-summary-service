package com.sample.weeklyreportsummaryservice.config;

import com.sample.weeklyreportsummaryservice.model.RedisKey;
import com.sample.weeklyreportsummaryservice.model.RedisValue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
public class RedisConfiguration {

    private final ApplicationConfig applicationConfig;

    public RedisConfiguration(ApplicationConfig applicationConfig) {
        this.applicationConfig = applicationConfig;
    }

    @Bean
    JedisConnectionFactory jedisConnectionFactory() {
//        RedisSentinelConfiguration sentinelConfig = new RedisSentinelConfiguration()
//                .master("mymaster")
//                .sentinel("127.0.0.1", 26379)
//                .sentinel("127.0.0.1", 26380);
        // TODO - For development purposes local standalone redis server is used. However proper redis cluster with sentinels should be used in production
        // TODO - set ttl
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration(applicationConfig.getRedisServer(),
                applicationConfig.getRedisPort());
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    public RedisTemplate<RedisKey, RedisValue> redisTemplate() {
        RedisTemplate<RedisKey, RedisValue> template = new RedisTemplate<>();
        template.setConnectionFactory(jedisConnectionFactory());
        return template;
    }
}
