package com.redis.estudo.redis.controller;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.redis.estudo.redis.config.RabbitMQConfig;
import com.redis.estudo.redis.service.RedisServices;


@RestController
@RequestMapping("/api/redis")
public class RedisController {
    
    @Autowired
    private RedisServices redisServices;

    @Autowired
    private RabbitTemplate rabbitTemplate; // quem vai enviar a mensagem para a fila do RabbitMQ

    //salvar dados no Redis com uma chave específica 
    @PostMapping("/save")
    public String savePerformance(@RequestBody Object data) {

        //1. Gerar  akey automaticamente usando UUID
        String key =  UUID.randomUUID().toString(); // Gerar uma chave única usando UUID
        
       //2. Salvar no redis usando o serviço 
        redisServices.savePerformance(key, data, 600); // Define um tempo de expiração de 60 segundos para a chave
        
        //3. Enviar a chave para a fila do RabbitMQ
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, key); // Envia a chave para a fila do RabbitMQ
        
        return "Dados salvos no Redis com a chave: " + key;
    }

    @GetMapping("/get/{key}")
    public Object get(@PathVariable String key) {
        String value = (String) redisServices.getPerformance(key);
        if ("null".equals(value) || value.isEmpty()) {
            return "Nenhum valor encontrado no Redis para a chave: " + key;
            
        }
        return "Valor recuperado do Redis com a chave: " + key + " - Valor: " + value;
    }

}
