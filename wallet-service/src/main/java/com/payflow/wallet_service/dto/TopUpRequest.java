package com.payflow.wallet_service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopUpRequest {

    @NotNull(message = "User Id is required")
    private Long userId;

    @NotNull(message = "Amount is required")
    @Positive
    private BigDecimal amount;
}
