package com.fundquest.assessment.transfers.helpers;

import com.fundquest.assessment.transfers.enums.TransferDirection;
import com.fundquest.assessment.users.User;
import com.fundquest.assessment.wallets.Wallet;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateTransferRequestDTO {
    private User user;
    private Wallet sourceWallet;
    private Wallet targetWallet;
    private Double amount;
    private TransferDirection direction;
}
