# 🚀 Projeto de Estudo: Spring Boot + Redis + RabbitMQ + SQLite

Este projeto foi desenvolvido com fins didáticos para explorar a integração entre tecnologias de **cache**, **mensageria** e **persistência de dados**. O objetivo principal é entender como desacoplar o recebimento de dados do seu processamento final, utilizando uma arquitetura assíncrona.

## 🛠️ Tecnologias Utilizadas

* **Java 17 & Spring Boot 3**
* **Redis:** Utilizado para cache de curto prazo, histórico rápido (Lists) e armazenamento temporário de chaves.
* **RabbitMQ:** Broker de mensagens para comunicação assíncrona entre o envio dos dados e a persistência.
* **SQLite:** Banco de dados relacional para persistência final dos dados processados.
* **Docker & Docker Compose:** Para orquestração dos serviços de Redis e RabbitMQ.

## 🏗️ Arquitetura do Fluxo de Dados

O projeto segue o seguinte fluxo:
1.  **API (POST):** Recebe um JSON, gera um UUID (chave), salva o dado no **Redis** com expiração (TTL) e envia essa chave para uma fila no **RabbitMQ**.
2.  **Mensageria:** O **RabbitMQ** retém a chave até que o consumidor esteja disponível.
3.  **Consumidor (Worker):** Escuta a fila, recupera a chave, busca o dado completo no **Redis** e realiza a persistência definitiva no **SQLite**.



[Image of microservices message queue architecture]


## 📋 Funcionalidades Implementadas

- [x] **CRUD no Redis:** Endpoints para salvar, buscar e deletar chaves.
- [x] **Mensageria com RabbitMQ:** Fluxo completo de Producer e Consumer.
- [x] **Histórico com Redis Lists:** Uso de `LPUSH` e `LTRIM` para manter um histórico dos últimos 10 itens processados em memória.
- [x] **Persistência Relacional:** Integração com SQLite via Spring Data JPA.

## 🚀 Como Rodar o Projeto

### Pré-requisitos
* Docker e Docker Compose instalados.
* JDK 17+.

### Passos
1.  **Subir os serviços (Redis e RabbitMQ):**
    ```bash
    docker-compose up -d
    ```
2.  **Rodar a aplicação Spring Boot:**
    ```bash
    ./mvnw spring-boot:run
    ```

## 🧪 Testes e Observações

### Teste de Estresse (Backpressure)
Para entender o poder da mensageria, foi realizado um teste de carga disparando múltiplas requisições simultâneas. 
* **Observação:** Mesmo que o banco de dados (SQLite) possua limitações de escrita simultânea, o **RabbitMQ** atua como um buffer, garantindo que nenhuma mensagem seja perdida durante o pico de tráfego.

### Painel de Monitoramento
O progresso das filas pode ser acompanhado via RabbitMQ Management em `http://localhost:15672` (guest/guest).

---
Desenvolvido por [João Victor Zambon](https://github.com/Abutoprod) para fins de estudo em arquitetura de sistemas.
