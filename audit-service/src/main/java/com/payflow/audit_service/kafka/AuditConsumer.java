package com.payflow.audit_service.kafka;

import com.payflow.audit_service.entity.AuditLog;
import com.payflow.audit_service.event.TransactionEvent;
import com.payflow.audit_service.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class AuditConsumer {

    private final AuditLogRepository auditLogRepository;

    @KafkaListener(topics = "transaction-events", groupId = "audit-group")
    public void handleTransactionEvent(TransactionEvent event){
        AuditLog auditLog = AuditLog.builder()
                .transactionId(event.getTransactionId())
                .senderId(event.getSenderId())
                .receiverId(event.getReceiverId())
                .amount(event.getAmount())
                .currency(event.getCurrency())
                .status(event.getStatus())
                .type(event.getType())
                .description(event.getDescription())
                .build();

        auditLogRepository.save(auditLog);
        log.info("Audit log saved for transaction: {}", event.getTransactionId());
    }}
