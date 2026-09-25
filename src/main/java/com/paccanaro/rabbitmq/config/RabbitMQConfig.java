package com.paccanaro.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String QUEUE_PAGAMENTOS = "pagamentos.queue";
    public static final String EXCHANGE_PAGAMENTOS = "pagamentos.exchange";
    public static final String ROUTING_KEY_PAGAMENTOS = "pagamentos.processar";

    @Bean
    public Queue filaPagamentos() {
        return new Queue(QUEUE_PAGAMENTOS, true);
    }

    @Bean
    public DirectExchange exchangePagamentos() {
        return new DirectExchange(EXCHANGE_PAGAMENTOS);
    }

    @Bean
    public Binding bindingPagamentos(Queue filaPagamentos, DirectExchange exchangePagamentos) {
        return BindingBuilder
                .bind(filaPagamentos)
                .to(exchangePagamentos)
                .with(ROUTING_KEY_PAGAMENTOS);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new JacksonJsonMessageConverter();
    }
}