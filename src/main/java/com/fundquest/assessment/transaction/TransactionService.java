package com.fundquest.assessment.transaction;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.transaction.helpers.CreateTransactionRequestDAO;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransactionService {

    private final HttpServletRequest httpServletRequest;
    private final TransactionRepository transactionRepository;

    public Transaction create(CreateTransactionRequestDAO request) {
        return transactionRepository.save(
                Transaction.builder()
                        .user(request.getUser())
                        .type(request.getType())
                        .amount(request.getAmount())
                        .status(request.getStatus())
                        .meta(buildTransactionMeta(request))
                        .build());
    }

    private Map<String, Object> buildTransactionMeta(CreateTransactionRequestDAO request) {
        Map<String, Object> meta = new HashMap<>();

        meta.put("ip", httpServletRequest.getLocalAddr());
        meta.put("user_agent", httpServletRequest.getHeader("user-agent"));

        return meta;
    }

}
