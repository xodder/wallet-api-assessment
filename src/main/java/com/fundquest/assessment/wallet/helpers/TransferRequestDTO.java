package com.fundquest.assessment.wallet.helpers;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.DecimalMin;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class TransferRequestDTO {
    @Nonnull
    private Long sourceWalletId;

    @Nonnull
    private Long targetWalletId;

    @Nonnull
    @DecimalMin("1.0")
    private Double amount;
}
