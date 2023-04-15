package com.fundquest.assessment.wallet;

import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.transaction.Transaction;
import com.fundquest.assessment.transaction.TransactionService;
import com.fundquest.assessment.transaction.enums.TransactionStatus;
import com.fundquest.assessment.transaction.enums.TransactionType;
import com.fundquest.assessment.transaction.helpers.CreateTransactionRequestDTO;
import com.fundquest.assessment.user.User;
import com.fundquest.assessment.wallet.deps.history.WalletBalanceHistory;
import com.fundquest.assessment.wallet.deps.history.WalletBalanceHistoryRepository;
import com.fundquest.assessment.wallet.deps.history.enums.WalletBalanceHistoryEvent;
import com.fundquest.assessment.wallet.deps.type.WalletType;
import com.fundquest.assessment.wallet.deps.type.WalletTypeRepository;
import com.fundquest.assessment.wallet.helpers.CreateWalletRequestDTO;
import com.fundquest.assessment.wallet.helpers.TransferRequestDTO;

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
                .orElseThrow(() -> new Exception("Wallet type not found"));

        return walletRepository.save(
                Wallet.builder()
                        .owner(issuer)
                        .type(walletType)
                        .balance(request.getInitialBalance())
                        .build());
    }

    public Wallet getById(Long id) throws Exception {
        return walletRepository.findById(id)
                .orElseThrow(() -> new Exception("Wallet not found")
                // PlatformException.builder().status(HttpStatus.NOT_FOUND).metaEntry("fields",
                // "hello") .build()
                );
    }

    public List<Wallet> getByOwnerId(Long ownerId) {
        return walletRepository.findByOwnerId(ownerId);
    }

    public Page<Wallet> getAll(Pageable pageable) {
        return walletRepository.findAll(pageable);
    }

    @Transactional(rollbackOn = { Exception.class })
    public Wallet transfer(TransferRequestDTO request) {
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
                CreateTransactionRequestDTO.builder()
                        .user(sourceWallet.getOwner())
                        .type(TransactionType.DEBIT)
                        .amount(request.getAmount())
                        .status(TransactionStatus.SUCCESSFUL)
                        .build());
        Wallet updatedSourceWallet = addTransactionToWallet(sourceWallet, senderTransaction);

        // process the receiver's side of the transaction
        Transaction receiverTransaction = transactionService.create(
                CreateTransactionRequestDTO.builder()
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
