package com.paccanaro.rabbitmq.repository;

import com.paccanaro.rabbitmq.model.Pagamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {
}
