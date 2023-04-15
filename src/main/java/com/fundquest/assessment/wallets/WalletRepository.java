package com.fundquest.assessment.wallets;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletRepository extends JpaRepository<Wallet, Long> {

    List<Wallet> findByOwnerId(Long userId);

}
