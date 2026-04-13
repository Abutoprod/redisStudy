package com.redis.estudo.redis.repository;
import com.redis.estudo.redis.model.MeuDado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DadosRepository extends JpaRepository<MeuDado, Long> {
}