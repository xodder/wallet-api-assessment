package com.fundquest.assessment.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.user.helpers.UserCreateRequestDAO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UserService {

    private final UserRepository userRepository;

    public Page<User> getAll(PageRequest request) {
        return userRepository.findAll(request);
    }

    public User create(UserCreateRequestDAO request) {
        return userRepository.save(
                User.builder()
                        .name(request.getName())
                        .email(request.getEmail())
                        .password(request.getPassword())
                        .build());
    }

}
