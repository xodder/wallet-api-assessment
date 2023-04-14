package com.fundquest.assessment.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fundquest.assessment.transaction.Transaction;
import com.fundquest.assessment.transaction.TransactionService;
import com.fundquest.assessment.wallet.Wallet;
import com.fundquest.assessment.wallet.WalletService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/v1/users")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserEndpoint {
    private static final String DEFAULT_PAGINATION_PAGE = "0";
    private static final String DEFAULT_PAGINATION_LIMIT = "20";

    private final UserService userService;
    private final WalletService walletService;
    private final TransactionService transactionService;

    @GetMapping(path = "")
    public ResponseEntity<Page<User>> getAll(
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGINATION_PAGE) Integer page,
            @RequestParam(name = "limit", defaultValue = DEFAULT_PAGINATION_LIMIT) Integer limit) {
        return ResponseEntity.ofNullable(userService.getAll(PageRequest.of(page, limit)));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getById(@PathVariable(name = "id") Long id) {
        return ResponseEntity.ofNullable(userService.getById(id));
    }

    @GetMapping(path = "/{id}/wallets")
    public ResponseEntity<List<Wallet>> getUserWallets(@PathVariable(name = "id") Long userId) {
        return ResponseEntity.ofNullable(walletService.getByOwnerId(userId));
    }

    @GetMapping(path = "/{id}/transactions")
    public ResponseEntity<Page<Transaction>> getUserTransactions(
            @PathVariable(name = "id") Long userId,
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGINATION_PAGE) Integer page,
            @RequestParam(name = "limit", defaultValue = "30") Integer limit) {
        return ResponseEntity.ofNullable(transactionService.getByUserId(userId, PageRequest.of(page, limit)));
    }

}
