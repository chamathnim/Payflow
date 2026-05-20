package com.payflow.audit_service.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {
    private Long transactionId;
    private String idempotencyKey;
    private Long senderId;
    private Long receiverId;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String type;
    private String description;
    private LocalDateTime createdAt;
}