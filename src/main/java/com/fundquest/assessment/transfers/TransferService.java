package com.fundquest.assessment.transfers;

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
import com.fundquest.assessment.transfers.enums.TransferDirection;
import com.fundquest.assessment.transfers.helpers.CreateTransferRequestDTO;
import com.fundquest.assessment.transfers.helpers.TransferRequestDTO;
import com.fundquest.assessment.users.User;
import com.fundquest.assessment.wallets.Wallet;
import com.fundquest.assessment.wallets.WalletService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransferService {
    private final TransferRepository transferRepository;
    private final WalletService walletService;
    private final TransactionService transactionService;

    public Page<Transfer> getByUserId(Long userId, Pageable pageable) {
        return transferRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public Page<Transfer> getAll(Pageable pageable) {
        return transferRepository.findAll(pageable);
    }

    public Transfer getById(Long id) throws Exception {
        return transferRepository.findById(id)
                .orElseThrow(() -> new PlatformException("Transfer record not found").setStatus(HttpStatus.NOT_FOUND));
    }

    @Transactional(rollbackOn = { Exception.class })
    public TransferRequestDTO transfer(User issuer, TransferRequestDTO request) throws Exception {
        Wallet sourceWallet = walletService.findById(request.getSourceWalletId()).orElseThrow(
                () -> new PlatformException("Source wallet does not exist").setStatus(HttpStatus.NOT_FOUND));
        Wallet targetWallet = walletService.findById(request.getTargetWalletId()).orElseThrow(
                () -> new PlatformException("Target wallet does not exist").setStatus(HttpStatus.NOT_FOUND));

        // is the right person the one making this request
        if (issuer.getId() != sourceWallet.getOwner().getId()) {
            throw new PlatformException("You cannot perform this action").setStatus(HttpStatus.FORBIDDEN);
        }

        // can this transfer be made?
        if (request.getAmount() > sourceWallet.getBalance())
            throw new PlatformException("Balance in wallet not enough").setStatus(HttpStatus.BAD_REQUEST)
                    .withMetaEntry("fields",
                            new HashMapBuilder<>().entry("amount", "Amount is greater then allowed").build());

        Double newBalanceAfterAction = sourceWallet.getBalance() - request.getAmount();
        if (newBalanceAfterAction < sourceWallet.getType().getMinimumBalance())
            throw new PlatformException(
                    "Transfer cannot be made because your remaining balance will be lower than the minimum balance allowed in this wallet")
                    .setStatus(HttpStatus.BAD_REQUEST).withMetaEntry("fields",
                            new HashMapBuilder<>().entry("amount", "Amount is greater than allowed").build());

        // create outward transfer record
        Transfer outTransfer = create(CreateTransferRequestDTO.builder()
                .user(sourceWallet.getOwner())
                .sourceWallet(sourceWallet)
                .targetWallet(targetWallet)
                .direction(TransferDirection.OUT)
                .amount(request.getAmount())
                .build());
        walletService.applyTransferTo(sourceWallet, outTransfer);

        // create inward transfer record
        Transfer inTransfer = create(CreateTransferRequestDTO.builder()
                .user(targetWallet.getOwner())
                .sourceWallet(sourceWallet)
                .targetWallet(targetWallet)
                .direction(TransferDirection.IN)
                .amount(request.getAmount())
                .build());
        walletService.applyTransferTo(targetWallet, inTransfer);

        return request;
    }

    private synchronized Transfer create(CreateTransferRequestDTO request) {
        Transaction transaction = transactionService.create(
                CreateTransactionRequestDTO.builder()
                        .user(request.getUser())
                        .wallet(request.getSourceWallet())
                        .type(request.getDirection() == TransferDirection.OUT ? TransactionType.DEBIT
                                : TransactionType.CREDIT)
                        .amount(request.getAmount())
                        .status(TransactionStatus.SUCCESSFUL)
                        .build());

        return transferRepository.save(
                Transfer.builder()
                        .user(request.getUser())
                        .sourceWallet(request.getSourceWallet())
                        .targetWallet(request.getTargetWallet())
                        .transaction(transaction)
                        .direction(request.getDirection())
                        .amount(request.getAmount())
                        .build());
    }
}
