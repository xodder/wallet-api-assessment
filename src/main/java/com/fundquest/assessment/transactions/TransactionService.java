package com.fundquest.assessment.transactions;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.lib.helpers.HashMapBuilder;
import com.fundquest.assessment.transactions.helpers.CreateTransactionRequestDTO;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionService {

    private final HttpServletRequest httpServletRequest;
    private final TransactionRepository transactionRepository;

    @Transactional(rollbackOn = { Exception.class })
    public Page<Transaction> getByUserId(Long userId, Pageable pageable) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public Transaction create(CreateTransactionRequestDTO request) {
        return transactionRepository.save(
                Transaction.builder()
                        .user(request.getUser())
                        .wallet(request.getWallet())
                        .type(request.getType())
                        .amount(request.getAmount())
                        .status(request.getStatus())
                        .meta(buildTransactionMeta(request))
                        .build());
    }

    private Map<String, Object> buildTransactionMeta(CreateTransactionRequestDTO request) {
        return new HashMapBuilder<String, Object>()
                .entry("ip", httpServletRequest.getLocalAddr())
                .entry("user_agent", httpServletRequest.getHeader("user-agent"))
                .build();

    }

}
