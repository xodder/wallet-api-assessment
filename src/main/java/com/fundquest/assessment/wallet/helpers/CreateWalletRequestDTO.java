package com.fundquest.assessment.wallet.helpers;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateWalletRequestDTO {
    @NotNull
    @Min(1)
    private Long walletTypeId;

    @Builder.Default
    private Double initialBalance = 0.0;
}
