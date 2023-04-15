package com.fundquest.assessment.wallets.deps.history;

import java.sql.Timestamp;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fundquest.assessment.transactions.Transaction;
import com.fundquest.assessment.wallets.Wallet;
import com.fundquest.assessment.wallets.deps.history.enums.WalletBalanceHistoryEvent;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "transaction_id")
    private Transaction transaction;

    @Column(name = "balance_before")
    private Double balanceBefore;

    @Column(name = "balance_after")
    private Double balanceAfter;

    @Enumerated(EnumType.STRING)
    @Column(name = "event")
    private WalletBalanceHistoryEvent event;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "recorded_at")
    private Timestamp recordedAt;

    // public Long getWalletId() {
    //     if (wallet == null)
    //         return null;
    //     return wallet.getId();
    // }

    // public Long getTransactionId() {
    //     if (transaction == null)
    //         return null;
    //     return transaction.getId();
    // }
}
