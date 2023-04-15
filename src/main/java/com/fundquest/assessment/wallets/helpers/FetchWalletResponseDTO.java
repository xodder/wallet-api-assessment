package com.fundquest.assessment.wallets.helpers;

import com.fundquest.assessment.wallets.Wallet;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FetchWalletResponseDTO {
    private Wallet wallet;
}
