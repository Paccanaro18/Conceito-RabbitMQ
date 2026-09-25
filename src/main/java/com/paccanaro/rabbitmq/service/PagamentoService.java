package com.paccanaro.rabbitmq.service;

import com.paccanaro.rabbitmq.config.RabbitMQConfig;
import com.paccanaro.rabbitmq.enums.StatusPagamento;
import com.paccanaro.rabbitmq.model.Pagamento;
import com.paccanaro.rabbitmq.repository.PagamentoRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;

@Service
public class PagamentoService {

    private final PagamentoRepository repository;
    private final RabbitTemplate rabbitTemplate;

    public PagamentoService(PagamentoRepository repository, RabbitTemplate rabbitTemplate) {
        this.repository = repository;
        this.rabbitTemplate = rabbitTemplate;
    }

    public Pagamento criarPagamento(UUID pagadorId, UUID recebedorId, BigDecimal valor, String descricao) {
        Pagamento pagamento = new Pagamento();
        pagamento.setPagadorId(pagadorId);
        pagamento.setRecebedorId(recebedorId);
        pagamento.setValor(valor);
        pagamento.setDescricao(descricao);
        pagamento.setStatus(StatusPagamento.PENDENTE);

        Pagamento salvo = repository.save(pagamento);

        rabbitTemplate.convertAndSend(
                RabbitMQConfig.EXCHANGE_PAGAMENTOS,
                RabbitMQConfig.ROUTING_KEY_PAGAMENTOS,
                salvo.getId()
        );

        return salvo;
    }
}