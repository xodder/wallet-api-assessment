package com.fundquest.assessment.wallet.helpers;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TransferRequestDAO {
    private Long sourceWalletId;
    private Long targetWalletId;
    private Float amount;
}
