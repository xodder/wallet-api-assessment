package com.fundquest.assessment.wallets;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.lib.exception.PlatformException;
import com.fundquest.assessment.lib.helpers.HashMapBuilder;
import com.fundquest.assessment.transactions.Transaction;
import com.fundquest.assessment.transfers.Transfer;
import com.fundquest.assessment.users.User;
import com.fundquest.assessment.wallets.deps.history.WalletBalanceHistory;
import com.fundquest.assessment.wallets.deps.history.WalletBalanceHistoryRepository;
import com.fundquest.assessment.wallets.deps.history.enums.WalletBalanceHistoryEvent;
import com.fundquest.assessment.wallets.deps.type.WalletType;
import com.fundquest.assessment.wallets.deps.type.WalletTypeRepository;
import com.fundquest.assessment.wallets.helpers.CreateWalletRequestDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTypeRepository walletTypeRepository;
    private final WalletBalanceHistoryRepository walletBalanceHistoryRepository;

    public Wallet create(User issuer, CreateWalletRequestDTO request) throws Exception {
        WalletType walletType = walletTypeRepository.findById(request.getWalletTypeId())
                .orElseThrow(() -> new PlatformException("Wallet type not found")
                        .setStatus(HttpStatus.NOT_FOUND).withMetaEntry("fields",
                                new HashMapBuilder<>()
                                        .entry("wallet_type_id", "A wallet type with this id does not exist")
                                        .build()));

        return walletRepository.save(
                Wallet.builder()
                        .owner(issuer)
                        .type(walletType)
                        .balance(request.getInitialBalance())
                        .build());
    }

    public Wallet getById(Long id) throws Exception {
        return walletRepository.findById(id)
                .orElseThrow(() -> new PlatformException("Wallet not found").setStatus(HttpStatus.NOT_FOUND));
    }

    public Optional<Wallet> findById(Long id) {
        return walletRepository.findById(id);
    }

    public List<Wallet> getByOwnerId(Long ownerId) {
        return walletRepository.findByOwnerId(ownerId);
    }

    public Page<Wallet> getAll(Pageable pageable) {
        return walletRepository.findAll(pageable);
    }

    public synchronized Wallet applyTransferTo(Wallet wallet, Transfer transfer) {
        Transaction transaction = transfer.getTransaction();
        Double newBalance = wallet.getBalance() + transaction.getSignedAmount();

        // add balance history record
        walletBalanceHistoryRepository.save(
                WalletBalanceHistory.builder()
                        .transaction(transaction)
                        .wallet(wallet)
                        .balanceBefore(wallet.getBalance())
                        .balanceAfter(newBalance)
                        .event(WalletBalanceHistoryEvent.of(transfer.getDirection()))
                        .build());

        // update wallet's balance
        wallet.setBalance(newBalance);

        return walletRepository.save(wallet);
    }
}
