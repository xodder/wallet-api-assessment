package com.fundquest.assessment.user.deps.role;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fundquest.assessment.user.deps.role.enums.RoleName;

public interface RoleRepository extends JpaRepository<Role, Long> {
    public Optional<Role> findByName(RoleName name);
}

