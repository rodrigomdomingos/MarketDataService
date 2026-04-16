package com.marketdata.infrastructure.adapters.outbound.messaging;

import com.marketdata.application.ports.out.MarketDataUpdatedPortOut;
import com.marketdata.infrastructure.config.RabbitMqConfig;
import java.time.OffsetDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.MessageDeliveryMode;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Component
@RequiredArgsConstructor
@Slf4j
public class RabbitMqMarketDataUpdatedPublisher implements MarketDataUpdatedPortOut {

    private final RabbitTemplate rabbitTemplate;

    @Override
    public void publishAfterCommit(Long stockId, OffsetDateTime snapshotAt) {
        Objects.requireNonNull(stockId, "stockId cannot be null");
        Objects.requireNonNull(snapshotAt, "snapshotAt cannot be null");

        if (!TransactionSynchronizationManager.isActualTransactionActive()) {
            throw new IllegalStateException("publishAfterCommit requires an active transaction");
        }

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                String payload = "{\"stockId\":%d,\"snapshotAt\":\"%s\"}"
                        .formatted(stockId, snapshotAt);

                rabbitTemplate.convertAndSend(
                        "",
                        RabbitMqConfig.MARKET_DATA_UPDATED_QUEUE,
                        payload,
                        message -> {
                            message.getMessageProperties().setContentType(MessageProperties.CONTENT_TYPE_JSON);
                            message.getMessageProperties().setDeliveryMode(MessageDeliveryMode.PERSISTENT);
                            return message;
                        }
                );

                log.debug("Published market data update event: stockId={}, snapshotAt={}", stockId, snapshotAt);
            }
        });
    }
}
