package com.marketdata.infrastructure.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    public static final String MARKET_DATA_UPDATED_QUEUE = "market.data.updated";

    @Bean
    public Queue marketDataUpdatedQueue() {
        return QueueBuilder.durable(MARKET_DATA_UPDATED_QUEUE).build();
    }
}
