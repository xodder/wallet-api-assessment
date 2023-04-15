package com.fundquest.assessment.deposits.helpers;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class DepositRequestDTO {
    @NotNull
    private Long targetWalletId;

    @NotNull
    @DecimalMin("1.0")
    private Double amount;
}
