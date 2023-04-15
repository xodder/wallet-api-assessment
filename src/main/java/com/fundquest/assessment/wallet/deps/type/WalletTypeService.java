package com.fundquest.assessment.wallet.deps.type;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.wallet.deps.type.helpers.CreateWalletTypeRequestDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletTypeService {
    private final WalletTypeRepository walletTypeRepository;

    public List<WalletType> getAll() {
        return walletTypeRepository.findAll();
    }

    public WalletType getById(Long id) throws Exception {
        return walletTypeRepository.findById(id).orElseThrow(() -> new Exception("Wallet type does not exist"));
    }

    public WalletType create(CreateWalletTypeRequestDTO request) throws Exception {
        if (walletTypeRepository.existsByNameIgnoreCase(request.getName())) {
            throw new Exception("Name already taken");
        }

        return walletTypeRepository.save(
                WalletType.builder()
                        .name(request.getName())
                        .minimumBalance(request.getMinimumBalance())
                        .monthlyInterestRate(request.getMonthlyInterestRate())
                        .build());
    }

}
