package com.fundquest.assessment.deposits.helpers;

import com.fundquest.assessment.users.User;
import com.fundquest.assessment.wallets.Wallet;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CreateDepositRequestDTO {
    private User user;
    private Wallet targetWallet;
    private Double amount;
}
