package config;

import lock.redis.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.TimeUnit;

@Slf4j
public class RedisConfig {
    @Bean
    public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate redisTemplate = new StringRedisTemplate(factory);
        return redisTemplate;
    }

    @Bean
    RedisLock.RedisWrapper redisWrapper(RedisTemplate redisTemplate) {
        return new RedisLock.RedisWrapper() {
            @Override
            public void delete(String key) {
                redisTemplate.delete(key);
            }

            @Override
            public boolean lock(String key, String value, long expireMills) {
                return redisTemplate.opsForValue().setIfAbsent(key, value, expireMills, TimeUnit.MILLISECONDS);
            }
        };
    }
}
