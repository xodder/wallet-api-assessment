package com.fundquest.assessment.users.deps.role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fundquest.assessment.users.deps.role.enums.RoleName;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RoleService {
    private final RoleRepository roleRepository;

    public Role resolve(RoleName name) {
        return roleRepository.findByName(name)
                .orElse(roleRepository.save(Role.builder().name(name).build()));
    }

}
