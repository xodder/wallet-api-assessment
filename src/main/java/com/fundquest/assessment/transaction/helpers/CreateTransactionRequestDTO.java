package com.fundquest.assessment.transaction.helpers;

import com.fundquest.assessment.transaction.enums.TransactionStatus;
import com.fundquest.assessment.transaction.enums.TransactionType;
import com.fundquest.assessment.user.User;
import com.fundquest.assessment.wallet.Wallet;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CreateTransactionRequestDTO {
    private User user;
    private Wallet wallet;
    private TransactionType type;
    private Double amount;
    private TransactionStatus status;
}
