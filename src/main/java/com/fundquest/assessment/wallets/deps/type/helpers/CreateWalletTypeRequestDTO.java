package com.fundquest.assessment.wallets.deps.type.helpers;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CreateWalletTypeRequestDTO {
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @NotNull(message = "Minimum balance is required")
    @PositiveOrZero(message = "Minimum balance cannot be less than zero")
    private Double minimumBalance;

    @NotNull(message = "Monthly interest rate is required")
    @PositiveOrZero(message = "Monthly interest rate cannot be less than zero")
    private Double monthlyInterestRate;
    
}
