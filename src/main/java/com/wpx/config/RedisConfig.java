package com.wpx.config;

import com.wpx.service.RedisBaseService;
import com.wpx.util.RedisCacheUtils;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * @Author: 不会飞的小鹏
 * @Description: 配置默认的Redis
 */
@Configuration
@EnableAutoConfiguration
public class RedisConfig {

    @Bean
    public RedisBaseService redisBaseService(RedisCacheUtils redisCacheUtils) {
        return new RedisBaseService(redisCacheUtils);
    }

    @Bean
    public RedisCacheUtils redisCacheUtils(StringRedisTemplate stringRedisTemplate) {
        return new RedisCacheUtils(stringRedisTemplate);
    }

}
