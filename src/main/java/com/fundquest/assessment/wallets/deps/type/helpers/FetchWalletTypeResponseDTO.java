package com.fundquest.assessment.wallets.deps.type.helpers;

import com.fundquest.assessment.wallets.deps.type.WalletType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchWalletTypeResponseDTO {
    private WalletType walletType;
}
