package com.alfonsoalmonte.ipinformation.config;

import com.alfonsoalmonte.ipinformation.model.IpResponse;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.net.UnknownHostException;
import java.time.Duration;

@Configuration
public class AutoCreateRedisConfig {
    @Bean
    public RedisTemplate<Object, IpResponse> movRedisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
        RedisTemplate<Object, IpResponse> template = new RedisTemplate();
        template.setConnectionFactory(redisConnectionFactory);

        // serializador
        Jackson2JsonRedisSerializer<IpResponse> ser = new Jackson2JsonRedisSerializer<IpResponse>(IpResponse.class);
        template.setDefaultSerializer(ser);
        return template;
    }
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(1))
                .disableCachingNullValues()
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
    }
    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return (builder) -> builder
                .withCacheConfiguration("ipresponse",
                        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(15)));
    }
}
