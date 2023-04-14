package com.fundquest.assessment.transaction;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.transaction.helpers.CreateTransactionRequestDTO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionService {

    private final HttpServletRequest httpServletRequest;
    private final TransactionRepository transactionRepository;

    public Page<Transaction> getByUserId(Long userId, Pageable pageable) {
        return transactionRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable);
    }

    public Transaction create(CreateTransactionRequestDTO request) {
        return transactionRepository.save(
                Transaction.builder()
                        .user(request.getUser())
                        .type(request.getType())
                        .amount(request.getAmount())
                        .status(request.getStatus())
                        .meta(buildTransactionMeta(request))
                        .build());
    }

    private Map<String, Object> buildTransactionMeta(CreateTransactionRequestDTO request) {
        Map<String, Object> meta = new LinkedHashMap<>();

        meta.put("ip", httpServletRequest.getLocalAddr());
        meta.put("user_agent", httpServletRequest.getHeader("user-agent"));

        return meta;
    }

}
