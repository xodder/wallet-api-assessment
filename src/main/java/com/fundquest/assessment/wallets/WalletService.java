package com.fundquest.assessment.wallets;

import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.lib.exception.PlatformException;
import com.fundquest.assessment.lib.helpers.HashMapBuilder;
import com.fundquest.assessment.transactions.Transaction;
import com.fundquest.assessment.transactions.TransactionService;
import com.fundquest.assessment.transactions.enums.TransactionStatus;
import com.fundquest.assessment.transactions.enums.TransactionType;
import com.fundquest.assessment.transactions.helpers.CreateTransactionRequestDTO;
import com.fundquest.assessment.users.User;
import com.fundquest.assessment.wallets.deps.history.WalletBalanceHistory;
import com.fundquest.assessment.wallets.deps.history.WalletBalanceHistoryRepository;
import com.fundquest.assessment.wallets.deps.history.enums.WalletBalanceHistoryEvent;
import com.fundquest.assessment.wallets.deps.type.WalletType;
import com.fundquest.assessment.wallets.deps.type.WalletTypeRepository;
import com.fundquest.assessment.wallets.helpers.CreateWalletRequestDTO;
import com.fundquest.assessment.wallets.helpers.TransferRequestDTO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTypeRepository walletTypeRepository;
    private final WalletBalanceHistoryRepository walletBalanceHistoryRepository;
    private final TransactionService transactionService;

    public Wallet create(User issuer, CreateWalletRequestDTO request) throws Exception {
        WalletType walletType = walletTypeRepository.findById(request.getWalletTypeId())
                .orElseThrow(() -> new PlatformException("Wallet type not found")
                        .setStatus(HttpStatus.NOT_FOUND).metaEntry("fields",
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

    @Transactional
    public Wallet getById(Long id) throws Exception {
        return walletRepository.findById(id)
                .orElseThrow(() -> new PlatformException("Wallet not found").setStatus(HttpStatus.NOT_FOUND));
    }

    @Transactional
    public List<Wallet> getByOwnerId(Long ownerId) {
        return walletRepository.findByOwnerId(ownerId);
    }

    public Page<Wallet> getAll(Pageable pageable) {
        return walletRepository.findAll(pageable);
    }

    @Transactional(rollbackOn = { Exception.class })
    public Wallet transfer(TransferRequestDTO request) throws Exception {
        Wallet sourceWallet = walletRepository.findById(request.getSourceWalletId()).orElseThrow(
                () -> new PlatformException("Source wallet does not exist").setStatus(HttpStatus.NOT_FOUND));
        Wallet targetWallet = walletRepository.findById(request.getTargetWalletId()).orElseThrow(
                () -> new PlatformException("Target wallet does not exist").setStatus(HttpStatus.NOT_FOUND));

        // can this transfer be made?
        if (request.getAmount() > sourceWallet.getBalance())
            throw new PlatformException("Balance in wallet not enough").setStatus(HttpStatus.BAD_REQUEST)
                    .metaEntry("fields",
                            new HashMapBuilder<>().entry("amount", "Amount is greater then allowed").build());

        Double newBalanceAfterAction = sourceWallet.getBalance() - request.getAmount();
        if (newBalanceAfterAction < sourceWallet.getType().getMinimumBalance())
            throw new PlatformException(
                    "Transfer cannot be made because your remaining balance will be lower than the minimum balance allowed in this wallet")
                    .setStatus(HttpStatus.BAD_REQUEST).metaEntry("fields",
                            new HashMapBuilder<>().entry("amount", "Amount is greater than allowed").build());

        // process the sender's side of the transaction
        Transaction senderTransaction = transactionService.create(
                CreateTransactionRequestDTO.builder()
                        .user(sourceWallet.getOwner())
                        .wallet(sourceWallet)
                        .type(TransactionType.DEBIT)
                        .amount(request.getAmount())
                        .status(TransactionStatus.SUCCESSFUL)
                        .build());
        Wallet updatedSourceWallet = addTransactionToWallet(sourceWallet, senderTransaction);

        // process the receiver's side of the transaction
        Transaction receiverTransaction = transactionService.create(
                CreateTransactionRequestDTO.builder()
                        .user(targetWallet.getOwner())
                        .wallet(targetWallet)
                        .type(TransactionType.CREDIT)
                        .amount(request.getAmount())
                        .status(TransactionStatus.SUCCESSFUL)
                        .build());
        addTransactionToWallet(targetWallet, receiverTransaction);

        return updatedSourceWallet;
    }

    private synchronized Wallet addTransactionToWallet(Wallet wallet, Transaction transaction) {
        Double newBalance = wallet.getBalance() + transaction.getSignedAmount();

        // add balance history record
        walletBalanceHistoryRepository.save(
                WalletBalanceHistory.builder()
                        .wallet(wallet)
                        .transaction(transaction)
                        .balanceBefore(wallet.getBalance())
                        .balanceAfter(newBalance)
                        .event(WalletBalanceHistoryEvent.of(transaction.getType()))
                        .build());

        // update wallet's balance
        wallet.setBalance(newBalance);

        return walletRepository.save(wallet);
    }

}
