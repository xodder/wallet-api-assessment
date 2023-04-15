package com.fundquest.assessment.transactions.helpers;

import com.fundquest.assessment.transactions.enums.TransactionStatus;
import com.fundquest.assessment.transactions.enums.TransactionType;
import com.fundquest.assessment.users.User;
import com.fundquest.assessment.wallets.Wallet;

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