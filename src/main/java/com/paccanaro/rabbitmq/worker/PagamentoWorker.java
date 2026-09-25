package com.paccanaro.rabbitmq.worker;

import com.paccanaro.rabbitmq.config.RabbitMQConfig;
import com.paccanaro.rabbitmq.enums.StatusPagamento;
import com.paccanaro.rabbitmq.model.Pagamento;
import com.paccanaro.rabbitmq.repository.PagamentoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
public class PagamentoWorker {

    private static final Logger log = LoggerFactory.getLogger(PagamentoWorker.class);

    private final PagamentoRepository repository;

    public PagamentoWorker(PagamentoRepository repository) {
        this.repository = repository;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_PAGAMENTOS)
    public void processar(UUID pagamentoId) {
        Optional<Pagamento> pagamentoOptional = repository.findById(pagamentoId);

        if (pagamentoOptional.isEmpty()) {
            log.warn("Pagamento {} não encontrado, ignorando mensagem", pagamentoId);
            return;
        }

        Pagamento pagamento = pagamentoOptional.get();

        if (pagamento.getStatus() != StatusPagamento.PENDENTE) {
            log.info("Pagamento {} já está com status {}, ignorando reprocessamento",
                    pagamentoId, pagamento.getStatus());
            return;
        }

        pagamento.setStatus(StatusPagamento.PROCESSANDO);
        repository.save(pagamento);

        boolean aprovado = chamarProvedorDePagamento(pagamento);

        pagamento.setStatus(aprovado ? StatusPagamento.APROVADO : StatusPagamento.RECUSADO);
        repository.save(pagamento);

        log.info("Pagamento {} processado com status {}", pagamentoId, pagamento.getStatus());
    }

    private boolean chamarProvedorDePagamento(Pagamento pagamento) {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return Math.random() > 0.1;
    }
}