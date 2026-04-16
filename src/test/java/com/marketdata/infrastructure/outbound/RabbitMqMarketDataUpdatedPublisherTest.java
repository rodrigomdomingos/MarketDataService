package com.marketdata.infrastructure.outbound;

import com.marketdata.infrastructure.adapters.outbound.messaging.RabbitMqMarketDataUpdatedPublisher;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class RabbitMqMarketDataUpdatedPublisherTest {

    @Mock
    private RabbitTemplate rabbitTemplate;

    @InjectMocks
    private RabbitMqMarketDataUpdatedPublisher rabbitMqMarketDataUpdatedPublisher;

    @AfterEach
    void clearTransactionSynchronization() {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.clearSynchronization();
        }
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.setActualTransactionActive(false);
        }
    }

    @Test
    void publishAfterCommitShouldRequireTransaction() {
        assertThatThrownBy(() -> rabbitMqMarketDataUpdatedPublisher.publishAfterCommit(
                1L,
                OffsetDateTime.parse("2026-04-15T20:00:00Z")
        )).isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("active transaction");

        verifyNoInteractions(rabbitTemplate);
    }
}
