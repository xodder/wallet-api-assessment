package com.fundquest.assessment.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fundquest.assessment.auth.helpers.LoginRequestDTO;
import com.fundquest.assessment.auth.helpers.RegisterRequestDTO;
import com.fundquest.assessment.lib.helpers.Response;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "/v1/auth")
public class AuthEndpoint {
    private final AuthService authService;

    @PostMapping(path = "login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO request) {
        return Response.of(authService.login(request));
    }

    @PostMapping(path = "register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestDTO request) throws Exception {
        return Response.of(authService.register(request));
    }
}
