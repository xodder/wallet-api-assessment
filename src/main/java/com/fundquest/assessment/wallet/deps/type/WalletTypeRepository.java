package com.fundquest.assessment.wallet.deps.type;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WalletTypeRepository extends JpaRepository<WalletType, Long> {
    public Boolean existsByNameIgnoreCase(String name);
}
