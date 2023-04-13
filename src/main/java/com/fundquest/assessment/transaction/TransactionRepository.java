package com.fundquest.assessment.transaction;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByUserId(Long userId, Pageable pageable);

    Page<Transaction> findByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable, Sort sort);

}
