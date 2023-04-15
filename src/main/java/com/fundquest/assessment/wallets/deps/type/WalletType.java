package com.fundquest.assessment.wallets.deps.type;

import java.util.List;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fundquest.assessment.wallets.Wallet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
@Table(name = "wallet_types")
public class WalletType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name")
    private String name;

    @Column(name = "minimum_balance")
    private Double minimumBalance;

    @Column(name = "monthly_interest_rate")
    private Double monthlyInterestRate;

    @ToString.Exclude
    @JsonBackReference
    @JsonIgnore
    @OneToMany(mappedBy = "type", fetch = FetchType.LAZY)
    @NotFound(action = NotFoundAction.IGNORE)
    private List<Wallet> wallets;

}
