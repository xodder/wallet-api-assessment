package com.fundquest.assessment.wallet.deps.type.helpers;

import com.fundquest.assessment.wallet.deps.type.WalletType;

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
