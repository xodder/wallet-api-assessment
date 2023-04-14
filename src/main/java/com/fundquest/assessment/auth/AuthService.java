package com.fundquest.assessment.auth;

import javax.swing.text.PlainView;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.auth.helpers.LoginRequestDTO;
import com.fundquest.assessment.auth.helpers.LoginResponseDTO;
import com.fundquest.assessment.auth.helpers.RegisterRequestDTO;
import com.fundquest.assessment.auth.helpers.RegisterResponseDTO;
import com.fundquest.assessment.auth.security.jwt.JwtUtils;
import com.fundquest.assessment.lib.exception.PlatformException;
import com.fundquest.assessment.user.User;
import com.fundquest.assessment.user.UserRepository;
import com.fundquest.assessment.user.deps.role.Role;
import com.fundquest.assessment.user.deps.role.RoleRepository;
import com.fundquest.assessment.user.deps.role.RoleService;
import com.fundquest.assessment.user.deps.role.enums.RoleName;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final RoleService roleService;

    public LoginResponseDTO login(LoginRequestDTO request) {
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
        if (userRepository.existsByEmail(request.getEmail())) {
            throw PlatformException.builder()
                    .message("Email is already in use")
                    .metaEntry("fields", null)
                    .build();
        }

        // create and persist user record
        User user = userRepository.save(
                User.builder()
                        .email(request.getEmail())
                        .password(encoder.encode(request.getPassword()))
                        .role(roleService.resolve(RoleName.ROLE_USER))
                        .build());

        // authenticate user and generate token
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtUtils.generateJwtToken(authentication);

        return RegisterResponseDTO.builder()
                .user(user)
                .token(token)
                .build();
    }

}
