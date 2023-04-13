package com.fundquest.assessment.wallet.deps.type;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@Entity
@Table(name = "wallet_types")
public class WalletType {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "minimum_balance")
    private Float minimumBalance;

    @Column(name = "monthly_interest_rate")
    private Float monthlyInterestRate;
}
