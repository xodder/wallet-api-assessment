package com.fundquest.assessment.wallet.helpers;

import com.fundquest.assessment.wallet.Wallet;

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
