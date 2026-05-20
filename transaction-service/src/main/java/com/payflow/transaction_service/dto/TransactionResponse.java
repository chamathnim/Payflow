package com.payflow.transaction_service.dto;

import com.payflow.transaction_service.entity.TransactionStatus;
import com.payflow.transaction_service.entity.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Value
@Builder
@AllArgsConstructor
public class TransactionResponse {

    Long id;
    String idempotencyKey;
    Long senderId;
    Long receiverId;
    BigDecimal amount;
    String currency;
    TransactionStatus status;
    TransactionType type;
    String description;
    LocalDateTime createdAt;
}
