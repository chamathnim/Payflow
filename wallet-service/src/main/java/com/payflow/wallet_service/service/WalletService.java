package com.payflow.wallet_service.service;

import com.payflow.wallet_service.dto.CreateWalletRequest;
import com.payflow.wallet_service.dto.TopUpRequest;
import com.payflow.wallet_service.dto.WalletResponse;
import com.payflow.wallet_service.entity.Wallet;
import com.payflow.wallet_service.exception.WalletAlreadyExistsException;
import com.payflow.wallet_service.exception.WalletNotFoundException;
import com.payflow.wallet_service.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletResponse createWallet(CreateWalletRequest request) {

        if (walletRepository.existsByUserId(request.getUserId())){
            throw new WalletAlreadyExistsException("A wallet already exists for user ID");
        }

        Wallet newWallet = Wallet.builder()
                .userId(request.getUserId())
                .balance(new BigDecimal("0"))
                .currency(request.getCurrency())
                .build();

        return mapToResponse(walletRepository.save(newWallet));

    }

    public WalletResponse getWallet(Long userId) {

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        return mapToResponse(wallet);
    }

    public WalletResponse topUp(TopUpRequest request) {

        Wallet wallet = walletRepository.findByUserId(request.getUserId())
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found"));

        wallet.setBalance(wallet.getBalance().add(request.getAmount()));

        return mapToResponse(walletRepository.save(wallet));
    }

    // Helper method - converts Wallet entity to WalletResponse
    private WalletResponse mapToResponse(Wallet wallet) {
        return WalletResponse.builder()
                .id(wallet.getId())
                .userId(wallet.getUserId())
                .balance(wallet.getBalance())
                .currency(wallet.getCurrency())
                .status(wallet.getWalletStatus())
                .createdAt(wallet.getCreatedAt())
                .build();
    }
}