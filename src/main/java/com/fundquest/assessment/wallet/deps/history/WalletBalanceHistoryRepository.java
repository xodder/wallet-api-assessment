package com.fundquest.assessment.wallet.deps.history;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletBalanceHistoryRepository extends JpaRepository<WalletBalanceHistory, Long> {

}
