package com.fundquest.assessment.wallets.deps.history;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletBalanceHistoryService {
    private final WalletBalanceHistoryRepository walletBalanceHistoryRepository;

    @Transactional
    public Page<WalletBalanceHistory> getByWalletId(Long walletId, Pageable pageable) {
        return walletBalanceHistoryRepository.findByWalletId(walletId, pageable);
    }

}
