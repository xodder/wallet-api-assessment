package com.fundquest.assessment.wallet.deps.history;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import com.fundquest.assessment.transaction.Transaction;
import com.fundquest.assessment.wallet.Wallet;
import com.fundquest.assessment.wallet.deps.history.enums.WalletBalanceHistoryEvent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    private LocalDateTime recordedAt;

}
