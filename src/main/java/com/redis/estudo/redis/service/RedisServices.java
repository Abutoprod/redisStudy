package com.redis.estudo.redis.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
@Service
public class RedisServices {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired  // Injeção do RedisTemplate para operações Redis
    public void savePerformance(String key, Object value, Integer expirationTimeInSeconds) {
       // Salva o valor no Redis usando a chave fornecida
        redisTemplate.opsForValue().set(key, value, Duration.ofSeconds(expirationTimeInSeconds)); // Define um tempo de expiração de 60 segundos para a chave
        System.out.println("Dados salvos no Redis com a chave: " + key);
    }

    public Object getPerformance(String key) {
        // Recupera o valor do Redis usando a chave fornecida
        Object value = redisTemplate.opsForValue().get(key);
        System.out.println("Dados recuperados do Redis para a chave: " + key);
        return value;
    }

    
    
}
