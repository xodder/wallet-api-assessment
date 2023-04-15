package com.fundquest.assessment.wallets;

import java.sql.Timestamp;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;
import org.hibernate.annotations.UpdateTimestamp;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fundquest.assessment.transactions.Transaction;
import com.fundquest.assessment.users.User;
import com.fundquest.assessment.wallets.deps.history.WalletBalanceHistory;
import com.fundquest.assessment.wallets.deps.type.WalletType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "wallets")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "owner_id")
    private User owner;

    @JsonManagedReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "type_id", nullable = false)
    private WalletType type;

    @Column(name = "balance")
    private Double balance;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    private Timestamp createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @ToString.Exclude
    @JsonBackReference
    @JsonIgnore
    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<Transaction> transactions;

    @ToString.Exclude
    @JsonBackReference
    @JsonIgnore
    @OneToMany(mappedBy = "wallet", fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<WalletBalanceHistory> balanceHistories;

}
