package com.redis.estudo.redis.service;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
@Service
public class RedisServices {
    
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public void addToHistory(String historyKey, String key) {
        // Adiciona a chave ao início da lista de histórico
        redisTemplate.opsForList().leftPush(historyKey, key);
        // Limita o tamanho da lista para os 100 itens mais recentes
        redisTemplate.opsForList().trim(historyKey, 0, 99);
    }
        // Busca a lista completa
    public java.util.List<Object> getRecentHistory(String listKey, int limit) {
        // LRANGE: Pega os dados do início até o limite definido
        return redisTemplate.opsForList().range(listKey, 0, limit - 1);
    }
    
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

    public boolean deletePerformance(String key) {
        // Deleta a chave do Redis
        Boolean result = redisTemplate.delete(key);
        if (result != null && result) {
            System.out.println("Chave deletada com sucesso: " + key);
            return true;
        } else {
            System.out.println("Nenhuma chave encontrada para deletar: " + key);
            return false;
        }
    }

    
    
}
