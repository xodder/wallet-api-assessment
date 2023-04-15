package com.fundquest.assessment.wallets.deps.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletBalanceHistoryRepository extends JpaRepository<WalletBalanceHistory, Long> {

    Page<WalletBalanceHistory> findByWalletId(Long walletId, Pageable pageable);

    Page<WalletBalanceHistory> findByWalletIdOrderByRecordedAtDesc(Long walletId, Pageable pageable);

}
