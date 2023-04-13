package com.fundquest.assessment.wallet.deps.type;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@Entity
@Table(name = "wallet_types")
public class WalletType {
    @Id
    @GeneratedValue
    @Column(name = "id")
    private Long id;

    @Column(name ="name")
    private String name;

    @Column(name = "minimum_balance")
    private Float minimumBalance;

    @Column(name = "monthly_interest_rate")
    private Float monthlyInterestRate;
}
