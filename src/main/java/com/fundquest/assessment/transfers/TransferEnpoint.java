package com.fundquest.assessment.transfers;

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

import com.fundquest.assessment.lib.helpers.Response;
import com.fundquest.assessment.transfers.helpers.TransferRequestDTO;
import com.fundquest.assessment.users.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(path = "/v1/transfers")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TransferEnpoint {
    private static final String DEFAULT_PAGINATION_PAGE = "0";
    private static final String DEFAULT_PAGINATION_LIMIT = "20";

    private final TransferService transferService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "")
    public ResponseEntity<?> getAll(
            @RequestParam(name = "page", defaultValue = DEFAULT_PAGINATION_PAGE) Integer page,
            @RequestParam(name = "limit", defaultValue = DEFAULT_PAGINATION_LIMIT) Integer limit) {
        return Response.named(transferService.getAll(PageRequest.of(page, limit)), "transfers");
    }

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id", required = true) Long id)
            throws Exception {
        return Response.named(transferService.getById(id), "transfer");
    }

    @PostMapping(path = "")
    public ResponseEntity<?> performTransfer(@Valid @RequestBody TransferRequestDTO request, Authentication authentication)
            throws Exception {
        User issuer = (User) authentication.getPrincipal();
        return Response.of(transferService.transfer(issuer, request));
    }

}
