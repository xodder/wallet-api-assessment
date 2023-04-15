package com.fundquest.assessment.transfers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Long> {
    public Page<Transfer> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
