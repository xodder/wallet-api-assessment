package com.fundquest.assessment.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fundquest.assessment.wallet.deps.history.WalletBalanceHistory;
import com.fundquest.assessment.wallet.deps.history.WalletBalanceHistoryService;
import com.fundquest.assessment.wallet.helpers.TransferRequestDAO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/wallets")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletEndpoint {
    private static final String DEFAULT_PAGINATION_PAGE = "0";
    private static final String DEFAULT_PAGINATION_LIMIT = "20";

    private final WalletService walletService;
    private final WalletBalanceHistoryService walletBalanceHistoryService;

    @GetMapping(path = "/")
    public ResponseEntity<Page<Wallet>> getAll(
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGINATION_PAGE) Integer page,
            @RequestParam(name = "limit", defaultValue = DEFAULT_PAGINATION_LIMIT) Integer limit) {

        return ResponseEntity.ofNullable(walletService.getAll(PageRequest.of(page, limit)));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Wallet> getById(@PathVariable(name = "id", required = true) Long id) {
        return ResponseEntity.ofNullable(walletService.getById(id));
    }

    @GetMapping(path = "/{id}/history")
    public ResponseEntity<Page<WalletBalanceHistory>> getWalletBalanceHistory(
            @PathVariable(name = "id", required = true) Long walletId,
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGINATION_PAGE) Integer page,
            @RequestParam(name = "limit", defaultValue = DEFAULT_PAGINATION_LIMIT) Integer limit) {

        return ResponseEntity
                .ofNullable(walletBalanceHistoryService.getByWalletId(walletId, PageRequest.of(page, limit)));
    }

    @PostMapping(path = "transfer") // could have been "{id}/transfer"
    public ResponseEntity<Wallet> performTransfer(@RequestBody TransferRequestDAO request) {
        return ResponseEntity.ofNullable(walletService.transfer(request));
    }

}
