package com.payflow.transaction_service.client;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class WalletClient {

    private final RestTemplate restTemplate;

    @Value("${services.wallet.url}")
    private String walletServiceUrl;

    // Check if wallet exists and has enough balance
    public BigDecimal getBalance(Long userId) {
        String url = walletServiceUrl + "/api/v1/wallets/" + userId;
        WalletResponse response = restTemplate.getForObject(url, WalletResponse.class);
        if (response == null) {
            throw new RuntimeException("Wallet not found for user: " + userId);
        }
        return response.getBalance();
    }

    // Deduct from sender
    public void topUp(Long userId, BigDecimal amount) {
        String url = walletServiceUrl + "/api/v1/wallets/topup";
        Map<String, Object> request = new HashMap<>();
        request.put("userId", userId);
        request.put("amount", amount);
        restTemplate.postForObject(url, request, Object.class);
    }

    // Simple response class to map wallet response
    @Data
    public static class WalletResponse {
        private Long id;
        private Long userId;
        private java.math.BigDecimal balance;
        private String currency;
        private String status;
    }
}