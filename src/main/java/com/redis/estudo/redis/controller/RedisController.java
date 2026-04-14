package com.redis.estudo.redis.controller;

import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
        redisServices.savePerformance(key, data, 6000); // Define um tempo de expiração de 60 segundos para a chave
        
        redisServices.addToHistory("history_list", key); // Adiciona a chave ao histórico de chaves no Redis

        //3. Enviar a chave para a fila do RabbitMQ
        rabbitTemplate.convertAndSend(RabbitMQConfig.QUEUE_NAME, key); // Envia a chave para a fila do RabbitMQ
        
        return "Dados salvos no Redis com a chave: " + key;
    }

         @GetMapping("/get/{key}")
        public ResponseEntity<String> get(@PathVariable String key) {
            // Pegue como Object primeiro para não dar erro de cast
            Object data = redisServices.getPerformance(key);

            if (data == null) {
                return ResponseEntity.status(404).body("Nenhum valor encontrado para a chave: " + key);
            }

            // Se você quer apenas exibir o texto do objeto:
            String value = data.toString(); 
            
            return ResponseEntity.ok("Valor recuperado: " + value);
        }   

        @DeleteMapping("/delete/{key}")
            public ResponseEntity<String> delete(@PathVariable String key) {
                boolean deleted = redisServices.deletePerformance(key);
                if (deleted) {
                    return ResponseEntity.ok("Chave deletada com sucesso: " + key);
                } else {
                    return ResponseEntity.status(404).body("Nenhuma chave encontrada para deletar: " + key);
                }
            }

        @GetMapping("/history/{limit}")
    public ResponseEntity<java.util.List<Object>> getHistory(@PathVariable int limit) {
        // Aqui você pode implementar a lógica para recuperar o histórico de chaves do Redis
        // Por exemplo, você pode usar redisTemplate.keys("*") para obter todas as chaves armazenadas
        // e retornar uma lista ou um resumo dessas chaves.

        java.util.List<Object> history = redisServices.getRecentHistory("history_list", limit);
        return ResponseEntity.ok(history);
    }
        
}
