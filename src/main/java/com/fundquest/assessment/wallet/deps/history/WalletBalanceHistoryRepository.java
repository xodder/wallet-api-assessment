package com.fundquest.assessment.wallet.deps.history;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletBalanceHistoryRepository extends JpaRepository<WalletBalanceHistory, Long> {

    Page<WalletBalanceHistory> findByWalletId(Long walletId, Pageable pageable, Sort sort);

    Page<WalletBalanceHistory> findByWalletIdOrderByRecordedAtDesc(Long walletId, Pageable pageable);

}
