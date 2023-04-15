package com.fundquest.assessment.deposits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.deposits.helpers.CreateDepositRequestDTO;
import com.fundquest.assessment.deposits.helpers.DepositRequestDTO;
import com.fundquest.assessment.lib.exception.PlatformException;
import com.fundquest.assessment.transactions.Transaction;
import com.fundquest.assessment.transactions.TransactionService;
import com.fundquest.assessment.transactions.enums.TransactionMethod;
import com.fundquest.assessment.transactions.enums.TransactionStatus;
import com.fundquest.assessment.transactions.enums.TransactionType;
import com.fundquest.assessment.transactions.helpers.CreateTransactionRequestDTO;
import com.fundquest.assessment.users.User;
import com.fundquest.assessment.wallets.Wallet;
import com.fundquest.assessment.wallets.WalletService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepositService {
    private final DepositRepository depositRepository;
    private final WalletService walletService;
    private final TransactionService transactionService;

    public Page<Deposit> getByUserId(Long userId, Pageable pageable) {
        return depositRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public Page<Deposit> getAll(Pageable pageable) {
        return depositRepository.findAll(pageable);
    }

    public Deposit getById(Long id) throws Exception {
        return depositRepository.findById(id)
                .orElseThrow(() -> new PlatformException("Deposit record not found").setStatus(HttpStatus.NOT_FOUND));
    }

    @Transactional(rollbackOn = { Exception.class })
    public Deposit deposit(User issuer, DepositRequestDTO request) throws Exception {
        Wallet targetWallet = walletService.findById(request.getTargetWalletId()).orElseThrow(
                () -> new PlatformException("Target wallet does not exist").setStatus(HttpStatus.NOT_FOUND));

        // is the right person the one making this request
        if (issuer.getId() != targetWallet.getOwner().getId()) {
            throw new PlatformException("You cannot perform this action").setStatus(HttpStatus.FORBIDDEN);
        }

        // create deposit record
        Deposit deposit = create(CreateDepositRequestDTO.builder()
                .user(targetWallet.getOwner())
                .targetWallet(targetWallet)
                .amount(request.getAmount())
                .build());
        walletService.applyDepositTo(targetWallet, deposit);

        return deposit;
    }

    private synchronized Deposit create(CreateDepositRequestDTO request) {
        Transaction transaction = transactionService.create(
                CreateTransactionRequestDTO.builder()
                        .user(request.getUser())
                        .wallet(request.getTargetWallet())
                        .type(TransactionType.CREDIT)
                        .method(TransactionMethod.DEPOSIT)
                        .amount(request.getAmount())
                        .status(TransactionStatus.SUCCESSFUL)
                        .build());

        return depositRepository.save(
                Deposit.builder()
                        .user(request.getUser())
                        .targetWallet(request.getTargetWallet())
                        .transaction(transaction)
                        .amount(request.getAmount())
                        .build());
    }
}
