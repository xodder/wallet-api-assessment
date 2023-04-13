package com.fundquest.assessment.wallet;

import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.transaction.Transaction;
import com.fundquest.assessment.transaction.TransactionService;
import com.fundquest.assessment.transaction.enums.TransactionStatus;
import com.fundquest.assessment.transaction.enums.TransactionType;
import com.fundquest.assessment.transaction.helpers.CreateTransactionRequestDAO;
import com.fundquest.assessment.wallet.deps.history.WalletBalanceHistory;
import com.fundquest.assessment.wallet.deps.history.WalletBalanceHistoryRepository;
import com.fundquest.assessment.wallet.deps.history.enums.WalletBalanceHistoryEvent;
import com.fundquest.assessment.wallet.helpers.CreateWalletRequestDAO;
import com.fundquest.assessment.wallet.helpers.TransferRequestDAO;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletBalanceHistoryRepository walletBalanceHistoryRepository;
    private final TransactionService transactionService;

    public Wallet create(CreateWalletRequestDAO request) {
        return walletRepository.save(
                Wallet.builder()
                        .owner(request.getUser())
                        .type(request.getType())
                        .balance(request.getInitialBalance())
                        .build());
    }

    public Wallet getById(Long id) {
        return walletRepository.findById(id).orElseThrow();
    }

    public List<Wallet> getByOwnerId(Long ownerId) {
        return walletRepository.findByOwnerId(ownerId);
    }

    @Transactional(rollbackOn = { Exception.class })
    public Wallet transfer(TransferRequestDAO request) {
        Wallet sourceWallet = walletRepository.findById(request.getSourceWalletId()).orElseThrow();
        Wallet targetWallet = walletRepository.findById(request.getTargetWalletId()).orElseThrow();

        // can this transfer be made?
        if (request.getAmount() > sourceWallet.getBalance())
            throw new ServiceException("Balance in wallet not enough");

        Float newBalanceAfterAction = sourceWallet.getBalance() - request.getAmount();
        if (newBalanceAfterAction < sourceWallet.getType().getMinimumBalance())
            throw new ServiceException(
                    "Transfer cannot be made because your remaining balance will be lower than the minimum balance allowed in this wallet");

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
