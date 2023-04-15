package com.fundquest.assessment.wallets.deps.history.enums;

import java.security.InvalidParameterException;

import com.fundquest.assessment.transactions.enums.TransactionType;
import com.fundquest.assessment.transfers.enums.TransferDirection;

public enum WalletBalanceHistoryEvent {
    TRANSFER_IN, TRANSFER_OUT, DEPOSIT;

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

    public static WalletBalanceHistoryEvent of(TransferDirection transferDirection) {
        switch (transferDirection) {
            case OUT:
                return TRANSFER_OUT;
            case IN:
                return TRANSFER_IN;
            default:
                throw new InvalidParameterException();
        }
    }
}
