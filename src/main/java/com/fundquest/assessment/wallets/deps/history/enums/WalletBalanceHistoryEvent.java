package com.fundquest.assessment.wallets.deps.history.enums;

import java.security.InvalidParameterException;

import com.fundquest.assessment.transactions.enums.TransactionType;

public enum WalletBalanceHistoryEvent {
    TRANSFER_IN, TRANSFER_OUT;

    public static WalletBalanceHistoryEvent of(TransactionType transactionType) {
        switch (transactionType) {
            case DEBIT:
                return TRANSFER_OUT;
            case CREDIT:
                return TRANSFER_IN;
            default:
                throw new InvalidParameterException();
        }
    }
}