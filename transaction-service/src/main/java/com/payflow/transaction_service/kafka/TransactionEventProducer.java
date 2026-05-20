package com.payflow.transaction_service.kafka;

import com.payflow.transaction_service.event.TransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TransactionEventProducer {

    private static final String TOPIC = "transaction-events";
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    public void publishTransactionEvent(TransactionEvent event) {
        log.info("Publishing transaction event: {}", event.getTransactionId());
        kafkaTemplate.send(TOPIC, event.getIdempotencyKey(), event);
    }
}