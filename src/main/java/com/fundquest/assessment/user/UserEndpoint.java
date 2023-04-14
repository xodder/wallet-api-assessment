package com.fundquest.assessment.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fundquest.assessment.lib.helpers.Response;
import com.fundquest.assessment.transaction.TransactionService;
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

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "")
    public ResponseEntity<?> getAll(
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGINATION_PAGE) Integer page,
            @RequestParam(name = "limit", defaultValue = DEFAULT_PAGINATION_LIMIT) Integer limit) {
        return Response.of(userService.getAll(PageRequest.of(page, limit)));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id) {
        return Response.of(userService.getById(id));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "/{id}/wallets")
    public ResponseEntity<?> getUserWallets(@PathVariable(name = "id") Long userId) {
        return Response.of(walletService.getByOwnerId(userId));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "/{id}/transactions")
    public ResponseEntity<?> getUserTransactions(
            @PathVariable(name = "id") Long userId,
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGINATION_PAGE) Integer page,
            @RequestParam(name = "limit", defaultValue = "30") Integer limit) {
        return Response.of(transactionService.getByUserId(userId, PageRequest.of(page, limit)));
    }
}
