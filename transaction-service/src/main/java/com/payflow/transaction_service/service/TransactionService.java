package com.payflow.transaction_service.service;

import com.payflow.transaction_service.client.WalletClient;
import com.payflow.transaction_service.dto.FraudCheckResult;
import com.payflow.transaction_service.dto.TransactionRequest;
import com.payflow.transaction_service.dto.TransactionResponse;
import com.payflow.transaction_service.entity.Transaction;
import com.payflow.transaction_service.entity.TransactionStatus;
import com.payflow.transaction_service.entity.TransactionType;
import com.payflow.transaction_service.event.TransactionEvent;
import com.payflow.transaction_service.exception.FraudulentTransactionException;
import com.payflow.transaction_service.exception.InsufficientBalanceException;
import com.payflow.transaction_service.exception.TransactionNotFoundException;
import com.payflow.transaction_service.kafka.TransactionEventProducer;
import com.payflow.transaction_service.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final FraudService fraudService;
    private final WalletClient walletClient;
    private final TransactionEventProducer transactionEventProducer;

    public TransactionResponse processTransaction(TransactionRequest request) {

        Optional<Transaction> existing = transactionRepository
                .findByIdempotencyKey(request.getIdempotencyKey());

        if (existing.isPresent()) {
            return mapToResponse(existing.get());
        }

        FraudCheckResult result = fraudService.check(request.getSenderId(), request.getAmount());

        if (result.isFraudulent()) throw new FraudulentTransactionException(result.getReason());

        if (walletClient.getBalance(request.getSenderId()).compareTo(request.getAmount()) < 0 ){
            throw new InsufficientBalanceException("Insufficient balance");
        }

        Transaction savedTransaction = transactionRepository.save(
                Transaction.builder()
                        .idempotencyKey(request.getIdempotencyKey())
                        .senderId(request.getSenderId())
                        .receiverId(request.getReceiverId())
                        .amount(request.getAmount())
                        .currency(request.getCurrency())
                        .status(TransactionStatus.PENDING)
                        .type(TransactionType.TRANSFER)
                        .description(request.getDescription())
                        .build()
        );

        try {
            walletClient.topUp(request.getSenderId(), request.getAmount().negate());
            walletClient.topUp(request.getReceiverId(), request.getAmount());
            savedTransaction.setStatus(TransactionStatus.COMPLETED);

            TransactionEvent event = TransactionEvent.builder()
                    .transactionId(savedTransaction.getId())
                    .idempotencyKey(savedTransaction.getIdempotencyKey())
                    .senderId(savedTransaction.getSenderId())
                    .receiverId(savedTransaction.getReceiverId())
                    .amount(savedTransaction.getAmount())
                    .currency(savedTransaction.getCurrency())
                    .status(savedTransaction.getStatus())
                    .type(savedTransaction.getType())
                    .description(savedTransaction.getDescription())
                    .createdAt(savedTransaction.getCreatedAt())
                    .build();

            transactionEventProducer.publishTransactionEvent(event);

        } catch (Exception e) {
            savedTransaction.setStatus(TransactionStatus.FAILED);
            walletClient.topUp(request.getSenderId(), request.getAmount());
            throw new RuntimeException("Transaction failed: " + e.getMessage());
        }

        return mapToResponse(transactionRepository.save(savedTransaction));

    }

    public TransactionResponse getTransaction(Long id) {

        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() ->  new TransactionNotFoundException("Transaction not found"));

        return mapToResponse(transaction);
    }

    public List<TransactionResponse> getUserTransactions(Long userId) {

        List<Transaction> sent = transactionRepository.findAllBySenderId(userId);
        List<Transaction> received = transactionRepository.findAllByReceiverId(userId);
        sent.addAll(received);

        return sent.stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .idempotencyKey(transaction.getIdempotencyKey())
                .senderId(transaction.getSenderId())
                .receiverId(transaction.getReceiverId())
                .amount(transaction.getAmount())
                .currency(transaction.getCurrency())
                .status(transaction.getStatus())
                .type(transaction.getType())
                .description(transaction.getDescription())
                .createdAt(transaction.getCreatedAt())
                .build();
    }
}