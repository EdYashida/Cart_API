package com.example.Cart.configs;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${broker.exchange.prod.name}")
    private String exchange;

    @Value("${broker.queue.prod.verificado}")
    private String queueVerificado;

    @Value("${broker.routing.prod.verificado}")
    private String keyVerificado;

    @Value("${broker.queue.prod.detalhado}")
    private String queueDetalhado;

    @Value("${broker.routing.prod.detalhado}")
    private String keyDetalhado;

    @Bean
    public Queue verificadoQueue() {
        return new Queue(queueVerificado, true);
    }

    @Bean
    public Queue detalhadoQueue() {
        return new Queue(queueDetalhado, true);
    }

    @Bean
    public DirectExchange produtoExchange() {
        return new DirectExchange(exchange);
    }

    @Bean
    public Binding verificadoBinding() {
        return BindingBuilder.bind(verificadoQueue())
                .to(produtoExchange())
                .with(keyVerificado);
    }

    @Bean
    public Binding detalhadoBinding() {
        return BindingBuilder.bind(detalhadoQueue())
                .to(produtoExchange())
                .with(keyDetalhado);
    }

    @Bean
    public Jackson2JsonMessageConverter messageConverter(){ //vai receber como Json e converter pra Java
        ObjectMapper objectMapper = new ObjectMapper();
        return new Jackson2JsonMessageConverter(objectMapper);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
            ConnectionFactory connectionFactory,
            Jackson2JsonMessageConverter messageConverter
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setMessageConverter(messageConverter);
        return factory;
    }
}
