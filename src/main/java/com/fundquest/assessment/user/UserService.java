package com.fundquest.assessment.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.lib.exception.PlatformException;
import com.fundquest.assessment.user.deps.role.RoleService;
import com.fundquest.assessment.user.deps.role.enums.RoleName;
import com.fundquest.assessment.user.helpers.CreateUserRequestDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final RoleService roleService;

    public Page<User> getAll(PageRequest request) {
        return userRepository.findAll(request);
    }

    public User create(CreateUserRequestDTO request) {
        return userRepository.save(
                User.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .role(roleService.resolve(RoleName.ROLE_USER))
                        .build());
    }

    public User getById(Long id) throws Exception {
        return userRepository.findById(id).orElseThrow(
                () -> new PlatformException("User not found")
                        .setStatus(HttpStatus.NOT_FOUND));
    }

    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User does not exist"));
    }

}
