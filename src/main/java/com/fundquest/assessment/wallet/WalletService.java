package com.fundquest.assessment.wallet;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.transaction.Transaction;
import com.fundquest.assessment.transaction.TransactionService;
import com.fundquest.assessment.transaction.enums.TransactionStatus;
import com.fundquest.assessment.transaction.enums.TransactionType;
import com.fundquest.assessment.transaction.helpers.CreateTransactionRequestDAO;
import com.fundquest.assessment.wallet.deps.history.WalletBalanceHistory;
import com.fundquest.assessment.wallet.deps.history.WalletBalanceHistoryRepository;
import com.fundquest.assessment.wallet.deps.history.enums.WalletBalanceHistoryEvent;
import com.fundquest.assessment.wallet.helpers.TransferRequestDAO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletBalanceHistoryRepository walletBalanceHistoryRepository;
    private final TransactionService transactionService;

    @Transactional(rollbackOn = { Exception.class })
    public Wallet transfer(TransferRequestDAO request) {
        Wallet sourceWallet = walletRepository.findById(request.getSourceWalletId()).orElseThrow();
        Wallet targetWallet = walletRepository.findById(request.getTargetWalletId()).orElseThrow();

        // can this transfer be made?
        if (request.getAmount() > sourceWallet.getBalance())
            throw new ServiceException("Balance in wallet not enough");

        // process the sender's side of the transaction
        Transaction senderTransaction = transactionService.create(
                CreateTransactionRequestDAO.builder()
                        .user(sourceWallet.getOwner())
                        .type(TransactionType.DEBIT)
                        .amount(request.getAmount())
                        .status(TransactionStatus.SUCCESSFUL)
                        .build());
        Wallet updatedSourceWallet = addTransactionToWallet(sourceWallet, senderTransaction);

        // process the receiver's side of the transaction
        Transaction receiverTransaction = transactionService.create(
                CreateTransactionRequestDAO.builder()
                        .user(targetWallet.getOwner())
                        .type(TransactionType.CREDIT)
                        .amount(request.getAmount())
                        .status(TransactionStatus.SUCCESSFUL)
                        .build());
        addTransactionToWallet(targetWallet, receiverTransaction);

        return updatedSourceWallet;
    }

    private synchronized Wallet addTransactionToWallet(Wallet wallet, Transaction transaction) {
        Float newBalance = wallet.getBalance() + transaction.getSignedAmount();

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
