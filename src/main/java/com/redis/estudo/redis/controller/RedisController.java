package com.redis.estudo.redis.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redis.estudo.redis.service.RedisServices;


@RestController
@RequestMapping("/api/redis")
public class RedisController {
    
    @Autowired
    private RedisServices redisServices;

    //salvar dados no Redis com uma chave específica 
    @PostMapping("/save")
    public String savePerformance(@RequestParam String key
                                , @RequestParam String value
                                , @RequestParam Integer expirationTimeInSeconds) {
        redisServices.savePerformance(key, value, expirationTimeInSeconds); // Define um tempo de expiração de 60 segundos para a chave
        return "Dados salvos no Redis com a chave: " + key;
    }

    @GetMapping("/get")
    public String getPerformance(@PathVariable String key) {
        String value = (String) redisServices.getPerformance(key);
        if ("null".equals(value) || value.isEmpty()) {
            return "Nenhum valor encontrado no Redis para a chave: " + key;
            
        }
        return "Valor recuperado do Redis com a chave: " + key + " - Valor: " + value;
    }

}
