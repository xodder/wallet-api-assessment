package com.fundquest.assessment.wallet.deps.history;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.fundquest.assessment.transaction.Transaction;
import com.fundquest.assessment.wallet.Wallet;
import com.fundquest.assessment.wallet.deps.history.enums.WalletBalanceHistoryEvent;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Entity
@Table(name = "wallet_balance_history")
public class WalletBalanceHistory {

    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", nullable = false)
    private Wallet wallet;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transaction transaction;

    @Column(name = "balance_before")
    private Float balanceBefore;

    @Column(name = "balance_after")
    private Float balanceAfter;

    @Enumerated(EnumType.STRING)
    @Column(name = "event")
    private WalletBalanceHistoryEvent event;

    @CreationTimestamp
    @Column(name = "recorded_at")
    private Timestamp recordedAt;

}
