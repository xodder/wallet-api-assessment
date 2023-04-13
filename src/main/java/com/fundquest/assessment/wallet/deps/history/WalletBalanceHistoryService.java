package com.fundquest.assessment.wallet.deps.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletBalanceHistoryService {
    private final WalletBalanceHistoryRepository walletBalanceHistoryRepository;

    public Page<WalletBalanceHistory> getByWalletId(Long walletId, Pageable pageable) {
        return walletBalanceHistoryRepository.findByWalletIdOrderByRecordedAtDesc(walletId, pageable);
    }

}
