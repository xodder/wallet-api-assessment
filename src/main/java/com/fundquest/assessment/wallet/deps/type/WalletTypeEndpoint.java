package com.fundquest.assessment.wallet.deps.type;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundquest.assessment.lib.helpers.Response;
import com.fundquest.assessment.wallet.deps.type.helpers.CreateWalletTypeRequestDTO;
import com.fundquest.assessment.wallet.deps.type.helpers.FetchWalletTypeResponseDTO;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "/v1/wallet-types")
public class WalletTypeEndpoint {
    private final WalletTypeService walletTypeService;

    @GetMapping(path = "")
    public ResponseEntity<?> getAll() {
        return Response.named(walletTypeService.getAll(), "types");
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<?> getById(@PathVariable(name = "id") Long id) throws Exception {
        return Response.of(new FetchWalletTypeResponseDTO(walletTypeService.getById(id)));
    }

    @PostMapping(path = "")
    public ResponseEntity<?> create(@RequestBody CreateWalletTypeRequestDTO request) throws Exception {
        return Response.of(new FetchWalletTypeResponseDTO(walletTypeService.create(request)));
    }

}
