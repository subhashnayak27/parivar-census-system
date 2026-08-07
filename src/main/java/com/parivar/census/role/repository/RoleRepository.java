package com.parivar.census.role.repository;

import com.parivar.census.role.entity.Role;
import com.parivar.census.role.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleName roleName);

    boolean existsByRoleName(RoleName roleName);
    List<Role> findByActiveTrue();
}