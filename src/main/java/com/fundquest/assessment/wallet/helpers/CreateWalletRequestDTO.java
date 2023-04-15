package com.fundquest.assessment.wallet.helpers;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateWalletRequestDTO {
    @Nonnull
    @Min(1)
    private Long walletTypeId;

    @Builder.Default
    private Double initialBalance = 0.0;
}
