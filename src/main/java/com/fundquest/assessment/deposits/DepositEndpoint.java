package com.fundquest.assessment.deposits;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fundquest.assessment.deposits.helpers.DepositRequestDTO;
import com.fundquest.assessment.lib.helpers.Response;
import com.fundquest.assessment.users.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/v1/deposits")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DepositEndpoint {
    private static final String DEFAULT_PAGINATION_PAGE = "0";
    private static final String DEFAULT_PAGINATION_LIMIT = "20";

    private final DepositService depositService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "")
    public ResponseEntity<?> getAll(
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGINATION_PAGE) Integer page,
            @RequestParam(name = "limit", defaultValue = DEFAULT_PAGINATION_LIMIT) Integer limit) {
        return Response.named(depositService.getAll(PageRequest.of(page, limit)), "deposits");
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id", required = true) Long id)
            throws Exception {
        return Response.named(depositService.getById(id), "deposit");
    }

    @PostMapping(path = "")
    public ResponseEntity<?> performDeposit(@Valid @RequestBody DepositRequestDTO request,
            Authentication authentication)
            throws Exception {
        User issuer = (User) authentication.getPrincipal();
        return Response.of(depositService.deposit(issuer, request));
    }

}
