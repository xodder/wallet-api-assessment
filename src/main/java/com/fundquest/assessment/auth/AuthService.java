package com.fundquest.assessment.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.auth.helpers.LoginRequestDTO;
import com.fundquest.assessment.auth.helpers.LoginResponseDTO;
import com.fundquest.assessment.auth.helpers.RegisterRequestDTO;
import com.fundquest.assessment.auth.helpers.RegisterResponseDTO;
import com.fundquest.assessment.auth.security.jwt.JwtUtils;
import com.fundquest.assessment.lib.exception.PlatformException;
import com.fundquest.assessment.lib.helpers.HashMapBuilder;
import com.fundquest.assessment.users.User;
import com.fundquest.assessment.users.UserService;
import com.fundquest.assessment.users.helpers.CreateUserRequestDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserService userService;
    private final PasswordEncoder encoder;

    public LoginResponseDTO login(LoginRequestDTO request) throws AuthenticationException {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);

        User user = (User) authentication.getPrincipal();

        return LoginResponseDTO.builder()
                .user(user)
                .token(token)
                .build();
    }

    public RegisterResponseDTO register(RegisterRequestDTO request) throws Exception {
        // check for email existence
        if (userService.existsByEmail(request.getEmail())) {
            throw new PlatformException("Email is already in use")
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .metaEntry("fields", new HashMapBuilder<String, String>()
                            .entry("email", "Email is already in use")
                            .build());
        }

        // create and persist user record
        User user = userService.create(
                CreateUserRequestDTO.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(encoder.encode(request.getPassword()))
                        .build());

        // authenticate user and generate token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(),
                        request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);

        return RegisterResponseDTO.builder()
                .user(user)
                .token(token)
                .build();
    }

}
