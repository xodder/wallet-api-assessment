package com.fundquest.assessment.transactions;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fundquest.assessment.lib.converters.JSONFieldConverter;
import com.fundquest.assessment.transactions.enums.TransactionStatus;
import com.fundquest.assessment.transactions.enums.TransactionType;
import com.fundquest.assessment.users.User;
import com.fundquest.assessment.wallets.Wallet;
import com.fundquest.assessment.wallets.deps.history.WalletBalanceHistory;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @JsonInclude(value = Include.NON_NULL)
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // @JsonIgnore
    @Basic(fetch = FetchType.LAZY)
    @JsonManagedReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet wallet;

    @Column(name = "type")
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    @Column(name = "amount")
    private Double amount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private TransactionStatus status;

    @Column(name = "meta")
    @Convert(converter = JSONFieldConverter.class)
    private Map<String, Object> meta;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Timestamp createdAt;

    @JsonIgnore
    @JsonBackReference
    @ToString.Exclude
    @OneToMany(mappedBy = "transaction", fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<WalletBalanceHistory> refWalletBalanceHistories;

    public Double getSignedAmount() {
        return type == TransactionType.DEBIT ? -1 * amount : amount;
    }
}
