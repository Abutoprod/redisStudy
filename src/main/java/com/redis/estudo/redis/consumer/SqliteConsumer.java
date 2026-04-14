package com.redis.estudo.redis.consumer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.redis.estudo.redis.config.RabbitMQConfig;
import com.redis.estudo.redis.model.MeuDado;
import com.redis.estudo.redis.repository.DadosRepository;
import com.redis.estudo.redis.service.RedisServices;

@Component
public class SqliteConsumer {

    @Autowired
    private RedisServices redisServices;
    @Autowired
    private DadosRepository repository; // Repositório para salvar os dados no SQLite
    
    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME) // Escuta a fila do RabbitMQ
    public void receiveMessage(String  key) {
        System.out.println("Chave recebida do RabbitMQ: " + key);
         
        //1. buscar os dados do Redis usando a chave recebida
        Object data = redisServices.getPerformance(key);
        if (data != null) {
            //2 transfrormar os dados em um objeto que possa ser salvo no SQLite
            MeuDado dadoParaBanco = new MeuDado();
            dadoParaBanco.setConteudo(data.toString()); // Exemplo de conversão, ajuste conforme necessário
            dadoParaBanco.setChave(key);
            repository.save(dadoParaBanco); // Salva o dado no SQLite usando o repositório
            System.out.println("Dados salvos no SQLite para a chave: " + key);
        }
        else {
            System.out.println("Nenhum dado encontrado no Redis para a chave: " + key);
        }
       
    }
}
