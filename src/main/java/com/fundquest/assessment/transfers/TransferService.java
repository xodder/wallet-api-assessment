package com.fundquest.assessment.transfers;

import java.util.List;

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
import com.fundquest.assessment.transfers.helpers.TransferRequestDTO;
import com.fundquest.assessment.wallets.Wallet;
import com.fundquest.assessment.wallets.WalletRepository;
import com.fundquest.assessment.wallets.WalletService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransferService {
    private final TransferRepository transferRepository;
    private final WalletService walletService;
    private final TransactionService transactionService;

    @Transactional
    public Page<Transfer> getByUserId(Long userId, Pageable pageable) {
        return transferRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public Page<Transfer> getAll(Pageable pageable) {
        return transferRepository.findAll(pageable);
    }

    @Transactional(rollbackOn = { Exception.class })
    public TransferRequestDTO transfer(TransferRequestDTO request) throws Exception {
        Wallet sourceWallet = walletService.getById(request.getSourceWalletId()).orElseThrow(
                () -> new PlatformException("Source wallet does not exist").setStatus(HttpStatus.NOT_FOUND));
        Wallet targetWallet = walletService.getById(request.getTargetWalletId()).orElseThrow(
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

        return request;
    }

    private Transfer create(CreateTransferRequestDTO request) {
        //
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
