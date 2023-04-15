package com.fundquest.assessment.transactions.helpers;

import com.fundquest.assessment.transactions.enums.TransactionMethod;
import com.fundquest.assessment.transactions.enums.TransactionStatus;
import com.fundquest.assessment.transactions.enums.TransactionType;
import com.fundquest.assessment.users.User;
import com.fundquest.assessment.wallets.Wallet;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CreateTransactionRequestDTO {
    @NotNull
    private User user;

    @NotNull
    private Wallet wallet;

    @NotNull
    private TransactionType type;

    @NotNull
    private TransactionMethod method;

    @NotNull
    private Double amount;

    @NotNull
    private TransactionStatus status;
}
