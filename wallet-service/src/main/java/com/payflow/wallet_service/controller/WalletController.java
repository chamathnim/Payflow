package com.payflow.wallet_service.controller;

import com.payflow.wallet_service.dto.CreateWalletRequest;
import com.payflow.wallet_service.dto.TopUpRequest;
import com.payflow.wallet_service.dto.WalletResponse;
import com.payflow.wallet_service.service.WalletService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallets")
public class WalletController {

    private final WalletService walletService;

    @PostMapping()
    public ResponseEntity<WalletResponse> createWallet(@RequestBody @Valid CreateWalletRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(walletService.createWallet(request));
    }

    @GetMapping("/{userId}")
    public ResponseEntity<WalletResponse> getWallet(@PathVariable Long userId){
        return ResponseEntity.ok(walletService.getWallet(userId));
    }

    @PostMapping("/topup")
    public ResponseEntity<WalletResponse> topUp(@RequestBody @Valid TopUpRequest request){
        return ResponseEntity.ok(walletService.topUp(request));
    }
}
