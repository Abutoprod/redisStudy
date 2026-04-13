package com.redis.estudo.redis.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    
    public static final String QUEUE_NAME = "redisQueue";
    
    @Bean
    public Queue redisQueue() {
        return new Queue(QUEUE_NAME, true); // true = duravel (não somente na memória, somente se o mq reinicia)
    }
}