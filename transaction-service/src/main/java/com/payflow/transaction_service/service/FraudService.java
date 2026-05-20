package com.payflow.transaction_service.service;

import com.payflow.transaction_service.dto.FraudCheckResult;
import com.payflow.transaction_service.entity.Transaction;
import com.payflow.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FraudService {

    private final TransactionRepository transactionRepository;

    private static final BigDecimal SUSPICIOUS_AMOUNT = new BigDecimal("10000");
    private static final int MAX_TRANSACTIONS_PER_MINUTE = 5;

    public FraudCheckResult check(Long senderId, BigDecimal amount) {

        if (amount.compareTo(SUSPICIOUS_AMOUNT) > 0){
            return FraudCheckResult.builder()
                    .fraudulent(true)
                    .reason("Amount exceeds suspicious threshold")
                    .build();
        }

        List<Transaction> transactions = transactionRepository.findAllBySenderId(senderId);

        List<Transaction> recentTransactions = transactions.stream()
                .filter(t -> t.getCreatedAt().isAfter(LocalDateTime.now().minusMinutes(1)))
                .toList();

        if (recentTransactions.size() > MAX_TRANSACTIONS_PER_MINUTE){
            return FraudCheckResult.builder()
                    .fraudulent(true)
                    .reason("Too many transactions in a short period")
                    .build();
        }

        return FraudCheckResult.builder()
                .fraudulent(false)
                .reason("OK")
                .build();
    }
}