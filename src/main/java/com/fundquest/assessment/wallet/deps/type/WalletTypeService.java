package com.fundquest.assessment.wallet.deps.type;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.lib.exception.PlatformException;
import com.fundquest.assessment.lib.helpers.HashMapBuilder;
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
        return walletTypeRepository.findById(id)
                .orElseThrow(() -> new PlatformException("Wallet type does not exist")
                        .setStatus(HttpStatus.NOT_FOUND));
    }

    public WalletType create(CreateWalletTypeRequestDTO request) throws Exception {
        if (walletTypeRepository.existsByNameIgnoreCase(request.getName())) {
            throw new PlatformException("Name already taken")
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .metaEntry("fields", new HashMapBuilder<>().entry("name", "Name is already in use").build());
        }

        return walletTypeRepository.save(
                WalletType.builder()
                        .name(request.getName())
                        .minimumBalance(request.getMinimumBalance())
                        .monthlyInterestRate(request.getMonthlyInterestRate())
                        .build());
    }

}
