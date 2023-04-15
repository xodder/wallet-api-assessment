package com.fundquest.assessment.deposits;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepositRepository extends JpaRepository<Deposit, Long> {
    public Page<Deposit> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
