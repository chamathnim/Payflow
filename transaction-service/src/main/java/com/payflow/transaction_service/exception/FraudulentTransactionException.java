package com.payflow.transaction_service.exception;

public class FraudulentTransactionException extends RuntimeException {

    public FraudulentTransactionException(String message) {
        super(message);
    }
}
