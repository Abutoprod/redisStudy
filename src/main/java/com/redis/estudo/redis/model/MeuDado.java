package com.redis.estudo.redis.model;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "registros_redis")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MeuDado {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String chave; 
    
    @Column(columnDefinition = "TEXT") // Define a coluna como TEXT para armazenar grandes quantidades de texto (como JSON)
    private String conteudo; // Armazena o conteúdo dos dados (pode ser JSON ou string)
    
}
