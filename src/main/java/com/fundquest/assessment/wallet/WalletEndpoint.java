package com.fundquest.assessment.wallet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fundquest.assessment.lib.helpers.Response;
import com.fundquest.assessment.wallet.deps.history.WalletBalanceHistoryService;
import com.fundquest.assessment.wallet.helpers.CreateWalletRequestDTO;
import com.fundquest.assessment.wallet.helpers.GetWalletResponseDTO;
import com.fundquest.assessment.wallet.helpers.TransferRequestDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/v1/wallets")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class WalletEndpoint {
    private static final String DEFAULT_PAGINATION_PAGE = "0";
    private static final String DEFAULT_PAGINATION_LIMIT = "20";

    private final WalletService walletService;
    private final WalletBalanceHistoryService walletBalanceHistoryService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "")
    public ResponseEntity<?> getAll(
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGINATION_PAGE) Integer page,
            @RequestParam(name = "limit", defaultValue = DEFAULT_PAGINATION_LIMIT) Integer limit) {
        return Response.of(walletService.getAll(PageRequest.of(page, limit)));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id", required = true) Long id)
            throws Exception {
        return Response.of(new GetWalletResponseDTO(walletService.getById(id)));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(path = "")
    public ResponseEntity<?> createWallet(@RequestBody CreateWalletRequestDTO request) {
        return Response.of(new GetWalletResponseDTO(walletService.create(request)));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "/{id}/history")
    public ResponseEntity<?> getWalletBalanceHistory(
            @PathVariable(name = "id", required = true) Long walletId,
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGINATION_PAGE) Integer page,
            @RequestParam(name = "limit", defaultValue = DEFAULT_PAGINATION_LIMIT) Integer limit) {

        return Response.of(walletBalanceHistoryService.getByWalletId(walletId, PageRequest.of(page, limit)));
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping(path = "transfer") // could have been "{id}/transfer"
    public ResponseEntity<?> performTransfer(@RequestBody TransferRequestDTO request) {
        return Response.of(new GetWalletResponseDTO(walletService.transfer(request)));
    }

}
