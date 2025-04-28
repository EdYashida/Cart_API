package com.example.Cart.poducers;

import com.example.Cart.dtos.ProdRecordDto;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CartProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Value("${broker.exchange.prod.name}")
    private String exchange;

    @Value("${broker.routing.prod.request}")
    private String produtoKey;

    public void requireMessageProd(String carId, String itemId){
        rabbitTemplate.convertAndSend(exchange, produtoKey, new ProdRecordDto(carId,"Requsitando dados de produto",itemId));

    }

}
