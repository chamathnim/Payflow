package com.payflow.notification_service.kafka;

import com.payflow.notification_service.event.TransactionEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationConsumer {

    @KafkaListener(topics = "transaction-events", groupId = "notification-group")
    public void handleTransactionEvent(TransactionEvent event){
        log.info("Notification received for transaction: {}", event.getTransactionId());
        log.info("Sender: {} → Receiver: {} | Amount: {} {}",
                event.getSenderId(), event.getReceiverId(),
                event.getAmount(), event.getCurrency());
        log.info("Status: {}", event.getStatus());
    }
}
