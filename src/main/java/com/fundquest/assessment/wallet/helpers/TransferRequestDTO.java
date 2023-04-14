package com.fundquest.assessment.wallet.helpers;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public class TransferRequestDTO {
    private Long sourceWalletId;
    private Long targetWalletId;
    private Float amount;
}
