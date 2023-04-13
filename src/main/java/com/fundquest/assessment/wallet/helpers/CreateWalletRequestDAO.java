package com.fundquest.assessment.wallet.helpers;

import com.fundquest.assessment.user.User;
import com.fundquest.assessment.wallet.deps.type.WalletType;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class CreateWalletRequestDAO {
    private User user;
    private WalletType type;
    private Float initialBalance;
}
