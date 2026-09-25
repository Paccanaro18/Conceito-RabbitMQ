package com.paccanaro.rabbitmq.controller;

import com.paccanaro.rabbitmq.model.Pagamento;
import com.paccanaro.rabbitmq.service.PagamentoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    private final PagamentoService service;

    public PagamentoController(PagamentoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Pagamento> criarPagamento(@RequestBody CriarPagamentoRequest request) {
        Pagamento pagamento = service.criarPagamento(
                request.pagadorId(),
                request.recebedorId(),
                request.valor(),
                request.descricao()
        );

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(pagamento);
    }

    public record CriarPagamentoRequest(
            UUID pagadorId,
            UUID recebedorId,
            BigDecimal valor,
            String descricao
    ) {}
}
