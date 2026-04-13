package com.redis.estudo.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        
        // Define como a CHAVE será escrita no Redis (texto puro)
        template.setKeySerializer(new StringRedisSerializer());
        
        // Define como o VALOR será escrito (JSON - para ser legível)
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        
        return template;
    }
}